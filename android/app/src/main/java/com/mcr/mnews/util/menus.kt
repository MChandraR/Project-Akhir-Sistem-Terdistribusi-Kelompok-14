package com.mcr.mnews.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class menus(var route:String, val title:String?, val icon: ImageVector){
    object  Home : menus("home","Home", Icons.Default.Home);
    object  Profile : menus("profile","Profile", Icons.Default.Person);
    object  Notif : menus("notification","Notification",Icons.Default.Notifications);
    object  Bookmark : menus("bookmark","Bookmark",Icons.Default.FavoriteBorder)
    object  Empty : menus("empty", "Empty",Icons.Default.Home);
}
