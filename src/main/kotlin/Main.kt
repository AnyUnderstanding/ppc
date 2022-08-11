// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.ui.window.application
import control.PPCApplication

// todo: stroke doesn't get completed when page is left
// todo: fix zero-width or zero-height boundingBox
// todo: selector actions like delete have no effect if mouse leaves page and therefor resets selectedPage

// todo: reduce memory
// todo: proposed solutions:
// todo: stroke has an global offset for the starting point afterwards only the between the points can be stored by using smaller datatypes like bytes
fun main() = application {
      PPCApplication(rememberApplicationState())
//    val a = 1.0
//    // override fun contains(value: T): Boolean = lessThanOrEquals(start, value) && lessThanOrEquals(value, endInclusive)
//    print(a in -1.0..3.0)

}







