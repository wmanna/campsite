package com.upgrade.campsite.dto;

import java.util.Set;

public class AvailableCalendar {

    public AvailableCalendar(Set<String> availableDates, int availableDatesTotal) {
        this.availableDates = availableDates;
        this.availableDatesTotal = availableDatesTotal;
    }

    private int availableDatesTotal;
    private Set<String> availableDates;

    public void setAvailableDates(Set<String> availableDates) {
        this.availableDates = availableDates;
    }

    public void setAvailableDatesTotal(int availableDatesTotal) {
        this.availableDatesTotal = availableDatesTotal;
    }

    public Set<String> getAvailableDates() {
        return availableDates;
    }

    public int getAvailableDatesTotal() {
        return availableDatesTotal;
    }
}
