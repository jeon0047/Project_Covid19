package com.cst2335.project_covid19;

public class Covid_Json {

    String category;
    String instructions;
    String favStatus;
    String q_country, country, case_num, case_date;

    public Covid_Json(){};
    //getItem(position).getCountry() + getItem(position).getCase_num() + getItem(position).getCase_date()
    public Covid_Json(String country, String case_num, String case_date) {
        this.country = country;
        this.case_num = case_num;
        this.case_date = case_date;
    }

    public String getCountry() {
        return this.country;
    }
    public String getCase_num() {
        return this.case_num;
    }
    public String getCase_date() {
        return this.case_date;
    }
}
