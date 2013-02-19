package it.betplus.birs.controller;

import it.betplus.web.framework.managedbeans.LanguageBean;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "languageBeanBirs")
@SessionScoped
public class LanguageBeanBirs extends LanguageBean {

	private static final long serialVersionUID = -1002653040276064609L;

	public LanguageBeanBirs() {
		super();
	}

	public Locale getItalianLocale() {
		return new Locale("it");
	}

}
