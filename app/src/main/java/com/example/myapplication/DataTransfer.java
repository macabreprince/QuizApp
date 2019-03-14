package com.example.myapplication;

import java.util.ArrayList;

public class DataTransfer {
   public static ArrayList<CategoriesReference> categoriesReferenceArrayList;
    public static ArrayList<CategoriesReference> putcategoriesReferenceArrayList(){
        if(categoriesReferenceArrayList==null)
            categoriesReferenceArrayList=new ArrayList<>();
        return categoriesReferenceArrayList;
    }
    public static ArrayList<CategoriesReference> getCategoriesReferenceArrayList(){
        return categoriesReferenceArrayList;
    }
}
