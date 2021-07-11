package com.example.docxter;

import android.provider.BaseColumns;

public class detailtable {
    public detailtable(){}
    public static final class detailtableinner implements BaseColumns
    {
        public static final String Table_Name="Scan_table";
        public static final String Column_Name="scan_number";
        public static final String timestamp="timeStamp";
    }
}
