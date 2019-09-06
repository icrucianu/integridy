/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.siveco.cad.integridy.controllers.util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author SIVECO
 */
@Named("localeManager")
@SessionScoped
public class LocaleManager implements Serializable{
    
    private Locale locale;

    private Language currentLanguage;
    private List<Language> languageList;
    
    public LocaleManager(){}
    
    @PostConstruct
    public void init(){
        languageList = new ArrayList<>();
        languageList.add(new Language("English", "en"));
        languageList.add(new Language("Turkish", "tr"));
        languageList.add(new Language("Romanian", "ro"));
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }
   
    public String getLanguage(){
        return locale.getLanguage();
    }
    public void setLanguage(String language){
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
    
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public Language getCurrentLanguage() {
        return currentLanguage;
    }
    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }
    public List<Language> getLanguageList() {
        return languageList;
    }
    public void setLanguageList(List<Language> languageList) {
        this.languageList = languageList;
    }
    
}
