package com.mcr.mnews.util

import com.mcr.mnews.R

sealed class Pengembang(var nim:String, var nama:String, var foto:Int, var desc:String ){
        object Chandra:Pengembang("2201020103","Muhammad Chandra Ramadhan",R.drawable.chan,"- Project Manager\n- Frontend Developer\n- Backend Developer")
        object Irfan:Pengembang("2201020100","Irfan Ibrahim",R.drawable.irfan,"UI Designer -\nServer Management -\nAsset Maker -")
        object Dimas:Pengembang("2201020118","Dimas Adrian Arifin",R.drawable.dimas,"- UI Designer\n- Debugger\n- Resources Provider")
        object Ridho:Pengembang("2201020104","Muhammad Ridho",R.drawable.ridho,"Database Management -\nServer Tester -\nServer Management")
        object Tiara:Pengembang("2101020007","Tiara Sonya",R.drawable.tiara,"- Backend Developer\n- Project Manager\n- Blackbox Tester")
        object Rizsky:Pengembang("2201020117","Rizsky Parsadanta Rajagukguk",R.drawable.rizsky,"Debugger -\nProject Tester -\nFeedback Giver -")
}
