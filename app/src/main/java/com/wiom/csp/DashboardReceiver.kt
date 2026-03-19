package com.wiom.csp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.QuizQuestion
import com.wiom.csp.data.Scenario
import com.wiom.csp.data.TrainingModule
import com.wiom.csp.util.Lang
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class DashboardReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "com.wiom.csp.SCENARIO" -> {
                val name = intent.getStringExtra("name") ?: "NONE"
                val scenario = try { Scenario.valueOf(name) } catch (_: Exception) { Scenario.NONE }
                if (scenario == Scenario.NONE) OnboardingState.clearScenario()
                else OnboardingState.triggerScenario(scenario)
            }
            "com.wiom.csp.NAVIGATE" -> {
                val screen = intent.getIntExtra("screen", -1)
                if (screen in 0 until OnboardingState.TOTAL_SCREENS) OnboardingState.goTo(screen)
            }
            "com.wiom.csp.LANG" -> {
                val lang = intent.getStringExtra("lang") ?: "toggle"
                when (lang) {
                    "hi" -> Lang.isHindi = true
                    "en" -> Lang.isHindi = false
                    else -> Lang.toggle()
                }
            }
            "com.wiom.csp.RESET" -> {
                OnboardingState.clearScenario()
                OnboardingState.currentScreen = 0
                OnboardingState.qaRejected = false
            }
            "com.wiom.csp.FILL" -> {
                val mode = intent.getStringExtra("mode") ?: "empty"
                if (mode == "filled") OnboardingState.fillAllScreens()
                else OnboardingState.emptyAllScreens()
            }
            "com.wiom.csp.TRAINING" -> {
                val configJson = intent.getStringExtra("config") ?: "[]"
                try {
                    val arr = JSONArray(configJson)
                    val modules = mutableListOf<TrainingModule>()
                    for (i in 0 until arr.length()) {
                        val m = arr.getJSONObject(i)
                        val questions = mutableListOf<QuizQuestion>()
                        val qArr = m.getJSONArray("questions")
                        for (j in 0 until qArr.length()) {
                            val q = qArr.getJSONObject(j)
                            val opts = mutableListOf<Pair<String, String>>()
                            val oArr = q.getJSONArray("options")
                            for (k in 0 until oArr.length()) {
                                val o = oArr.getJSONArray(k)
                                opts.add(o.getString(0) to o.getString(1))
                            }
                            questions.add(QuizQuestion(
                                questionHi = q.getString("questionHi"),
                                questionEn = q.getString("questionEn"),
                                options = opts,
                                correctIndex = q.getInt("correctIndex"),
                                hintHi = q.getString("hintHi"),
                                hintEn = q.getString("hintEn"),
                            ))
                        }
                        modules.add(TrainingModule(
                            id = m.getString("id"),
                            titleHi = m.getString("titleHi"),
                            titleEn = m.getString("titleEn"),
                            subtitleHi = m.optString("subtitleHi", ""),
                            subtitleEn = m.optString("subtitleEn", ""),
                            icon = m.optString("icon", "📚"),
                            videoUrl = m.optString("videoUrl", ""),
                            questions = questions,
                        ))
                    }
                    OnboardingState.trainingModules.clear()
                    OnboardingState.trainingModules.addAll(modules)
                    OnboardingState.completedModuleIds.clear()
                } catch (_: Exception) { }
            }
            "com.wiom.csp.QA" -> {
                val action = intent.getStringExtra("action") ?: "approved"
                when (action) {
                    "approved" -> {
                        OnboardingState.qaRejected = false
                        OnboardingState.goTo(7) // Move to Policy & Rate Card
                    }
                    "rejected" -> {
                        OnboardingState.qaRejected = true
                        OnboardingState.goTo(6) // Stay on QA screen, show rejected
                    }
                }
            }
            "com.wiom.csp.DUMP_STATE" -> {
                val s = OnboardingState
                val json = JSONObject().apply {
                    put("currentScreen", s.currentScreen)
                    put("qaRejected", s.qaRejected)
                    put("isFilledMode", s.isFilledMode)

                    // Personal Information
                    put("personal", JSONObject().apply {
                        put("phone", s.phoneNumber)
                        put("name", s.personalName)
                        put("email", s.personalEmail)
                        put("entityType", s.entityType)
                        put("tradeName", s.tradeName)
                    })

                    // Location Information
                    put("location", JSONObject().apply {
                        put("city", s.city)
                        put("pincode", s.pincode)
                        put("address", s.address)
                    })

                    // KYC Documents
                    put("kyc", JSONObject().apply {
                        put("panUploaded", s.panUploaded)
                        put("aadhaarFrontUploaded", s.aadhaarFrontUploaded)
                        put("aadhaarBackUploaded", s.aadhaarBackUploaded)
                        put("gstUploaded", s.gstUploaded)
                    })

                    // Registration Fee
                    put("registrationFee", JSONObject().apply {
                        put("amount", 2000)
                        put("paid", s.currentScreen > 5)
                    })

                    // Bank Account Details
                    put("bank", JSONObject().apply {
                        put("accountHolder", s.bankAccountHolder)
                        put("bankName", s.bankName)
                        put("accountNumber", s.bankAccountNumber)
                        put("ifsc", s.bankIfsc)
                        put("verified", s.bankVerified)
                    })

                    // Technical Review
                    put("techReview", JSONObject().apply {
                        put("shopPhotoUploaded", s.shopPhotoUploaded)
                        put("equipmentReviewed", s.equipmentReviewed)
                        put("internetSetupType", s.internetSetupType)
                    })

                    // Onboarding Fee
                    put("onboardingFee", JSONObject().apply {
                        put("amount", 20000)
                        put("paid", s.currentScreen > 11)
                    })

                    // Active scenario
                    put("activeScenario", s.activeScenario.name)

                    // Training progress
                    put("training", JSONObject().apply {
                        put("totalModules", s.trainingModules.size)
                        put("completedModules", s.completedModuleIds.size)
                        put("allCompleted", s.allModulesCompleted())
                    })
                }

                val file = File(context.filesDir, "state.json")
                file.writeText(json.toString(2))
            }
        }
    }
}
