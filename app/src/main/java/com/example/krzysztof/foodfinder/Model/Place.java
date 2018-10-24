package com.example.krzysztof.foodfinder.Model;

public class Place {

    private String logoURL;
    private String name;
    private String isOpen;
    private String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Place()
    {
        this.logoURL = "";
        this.name = "";
        this.isOpen = "";
        this.address= "";
    }

    public Place(String logoURL, String name, String isOpen, String address) {
        this.logoURL = logoURL;
        this.name = name;

        if(isOpen=="TRUE")
            this.isOpen = "OTWARTE";
        else if(isOpen=="FALSE")
            this.isOpen = "ZAMKNIĘTE";
        else
            this.isOpen = "BRAK DANYCH";


        this.address= address;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isOpen() {
        return isOpen;
    }

    public void setOpen(String open) {
        isOpen= open;
    }
}
