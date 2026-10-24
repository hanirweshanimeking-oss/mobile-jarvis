package com.hanirweshanimeking.mobilejarvis

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.hanirweshanimeking.mobilejarvis.ui.MobileJarvisApp
import com.hanirweshanimeking.mobilejarvis.ui.theme.MobileJarvisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileJarvisTheme {
                MobileJarvisApp()
            }
        }
    }
}
