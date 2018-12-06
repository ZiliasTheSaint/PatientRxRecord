package resources;

import java.util.ListResourceBundle;

/**
 * Main class resources! <br>
 * 
 * 
 * @author Dan Fulea, 14 Jun. 2015
 */
public class Resources extends ListResourceBundle {

	/**
	 * Returns the array of strings in the resource bundle.
	 * 
	 * @return the resources.
	 */
	public Object[][] getContents() {
		// TODO Auto-generated method stub
		return CONTENTS;
	}

	/** The resources to be localised. */
	private static final Object[][] CONTENTS = {

			// displayed images..
			{ "form.icon.url", "/danfulea/resources/personal.png" },///jdf/resources/duke.png" },
			{ "icon.url", "/danfulea/resources/personal.png" },///jdf/resources/globe.gif" },

			{ "img.zoom.in", "/danfulea/resources/zoom_in.png" },
			{ "img.zoom.out", "/danfulea/resources/zoom_out.png" },
			{ "img.pan.left", "/danfulea/resources/arrow_left.png" },
			{ "img.pan.up", "/danfulea/resources/arrow_up.png" },
			{ "img.pan.down", "/danfulea/resources/arrow_down.png" },
			{ "img.pan.right", "/danfulea/resources/arrow_right.png" },
			{ "img.pan.refresh", "/danfulea/resources/arrow_refresh.png" },

			{ "img.accept", "/danfulea/resources/accept.png" },
			{ "img.insert", "/danfulea/resources/add.png" },
			{ "img.delete", "/danfulea/resources/delete.png" },
			{ "img.delete.all", "/danfulea/resources/bin_empty.png" },
			{ "img.view", "/danfulea/resources/eye.png" },
			{ "img.set", "/danfulea/resources/cog.png" },
			{ "img.report", "/danfulea/resources/document_prepare.png" },
			{ "img.today", "/danfulea/resources/time.png" },
			{ "img.open.file", "/danfulea/resources/open_folder.png" },
			{ "img.open.database", "/danfulea/resources/database_connect.png" },
			{ "img.save.database", "/danfulea/resources/database_save.png" },
			{ "img.substract.bkg", "/danfulea/resources/database_go.png" },
			{ "img.close", "/danfulea/resources/cross.png" },
			{ "img.about", "/danfulea/resources/information.png" },
			{ "img.printer", "/danfulea/resources/printer.png" },

			{ "Application.NAME", "Patient RX records - KAP evaluation" },
			{ "Application.NAME.RO", "Informatii pacienti - Evaluare KAP" },
			{ "About.NAME", "About" },
			{ "About.NAME.RO", "Despre" },
			{ "HowTo.NAME", "Useful tips" },
			{ "HowTo.NAME.RO", "Informatii utile" },
			{ "Language.NAME", "Select language" },
			{ "Language.NAME.RO", "Selecteaza limba" },
			
			{ "language.Cb", new String[] { "English", "Romana" } },
			{ "language.button.set", "Set" },
			{ "language.button.set.toolTip", "Set language" },
			{ "language.button.set.mnemonic", new Character('S') },
			
			{ "Author", "Author:" },
			{ "Author.RO", "Autor:" },
			{ "Author.name", "Dan Fulea , fulea.dan@gmail.com" },

			{ "Version", "Version:" },
			{ "Version.RO", "Versiune:" },
			{ "Version.name", "PatientRX 1.1" },

			{
					"License",
					// "This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License (version 2) as published by the Free Software Foundation. \n\nThis program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. \n"
					// },
					"Copyright (c) 2014, Dan Fulea \nAll rights reserved.\n\nRedistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:\n\n1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.\n\n2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.\n\n3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.\n\nTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 'AS IS' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" },

			// =================
			{ "menu.file", "File" },
			{ "menu.file.mnemonic", new Character('F') },

			{ "menu.file.report", "Generate report" },
			{ "menu.file.report.RO", "Generare raport" },
			{ "menu.file.report.mnemonic", new Character('G') },{ "menu.file.report.mnemonic.RO", new Character('G') },
			{ "menu.file.report.toolTip",
					"Generate report to be saved in various format such as PDF, DOCX, XLS, ..." },
			{ "menu.file.report.toolTip.RO",
					"Genereaza report pentru a fi salvat in diferite formate cum ar fi: PDF, DOCX, XLS, ..." },

			{ "menu.file.lang", "Change language..." },{ "menu.file.lang.RO", "Schimba limba..." },
			{ "menu.file.lang.mnemonic", new Character('L') },{ "menu.file.lang.mnemonic.RO", new Character('l') },
			{ "menu.file.lang.toolTip", "Change main application language" },
			{ "menu.file.lang.toolTip.RO", "Schimba limba aplicatiei principale" },

			{ "menu.file.exit", "Close" },{ "menu.file.exit.RO", "Inchide" },
			{ "menu.file.exit.mnemonic", new Character('C') },{ "menu.file.exit.mnemonic.RO", new Character('I') },
			{ "menu.file.exit.toolTip", "Close the application" },
			{ "menu.file.exit.toolTip.RO", "Inchide aplicatia" },

			{ "menu.help", "Help" },
			{ "menu.help.mnemonic", new Character('H') },

			{ "menu.help.about", "About..." },{ "menu.help.about.RO", "Despre..." },
			{ "menu.help.about.mnemonic", new Character('A') },{ "menu.help.about.mnemonic.RO", new Character('D') },
			{ "menu.help.about.toolTip",
					"Several informations about this application" },
			{ "menu.help.about.toolTip.RO",
					"Cateva informatii despre aceasta aplicatie" },		

			{ "menu.help.LF", "Look and feel..." },
			{ "menu.help.LF.mnemonic", new Character('L') },
			{ "menu.help.LF.toolTip", "Change application look and feel" },

			{ "menu.help.info", "How to..." },
			{ "menu.help.info.mnemonic", new Character('H') },
			{ "menu.help.info.toolTip", "How to..." },
			{ "menu.help.info.RO", "Informatii utile..." },
			{ "menu.help.info.mnemonic.RO", new Character('u') },
			{ "menu.help.info.toolTip.RO", "Informatii utile" },
			// =====================================

			{ "number.error", "Insert valid positive numbers! " },
			{ "number.error.title", "Error" },
			
			{ "number.error.RO", "Introduceti numere reale pozitive " },
			{ "number.error.title.RO", "Eroare" },
			{
					"dialog.ripple",
					"If anode=Mo or Rh, angle>=9,angle <=23;kv>=25,kv<=32;ripple=0; \n if anod=W, angle >=6 angle<=22;kv>=30;kv<=150!! \n for ripple=>kv =55,60,65,...,90!!" },

			{
					"dialog.ripple.RO",
					"Daca anodul=Mo or Rh, unghi>=9,unghi <=23;kv>=25,kv<=32;ripple=0; \n Daca anodul=W, unghi >=6 unghi<=22;kv>=30;kv<=150!! \n Pentru ondulatie=>kv =55,60,65,...,90!!" },

			{ "dialog.exit.title", "Confirm..." },
			{ "dialog.exit.message", "Are you sure?" },
			{ "dialog.exit.buttons", new Object[] { "Yes", "No" } },
			{ "dialog.exit.title.RO", "Confirmare..." },
			{ "dialog.exit.message.RO", "Sunteti sigur?" },
			{ "dialog.exit.buttons.RO", new Object[] { "Da", "Nu" } },
			// ========DB and
			// records@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			{ "main.db", "patientrx" },
			{ "main.db.Table", "patientinfo" },// main table

			{ "data.load", "Data" },
			{ "sort.by", "Sort by: " },
			{ "records.count", "Records count: " },
			{ "sort.by.RO", "Sortat dupa: " },
			{ "records.count.RO", "Numar inregistrari: " },
			
			{ "records.border", "Records" },
			{ "records.label", "Records:" },
			{ "records.border.RO", "Inregistrari" },
			{ "records.label.RO", "Inregistrari:" },

			{ "loadUnit.ext", "unt" },
			{ "loadUnit.description", "Medical unit file" },
			{ "data.loadUnit", "Data" },
			// ========GUI==========================
			{ "MainForm.tab.data", "Settings" },
			{ "MainForm.tab.results", "Database" },
			{ "MainForm.tab.data.RO", "Setari" },
			{ "MainForm.tab.results.RO", "Baza de date" },

			{ "main.button.today", "Today" },
			{ "main.button.today.toolTip", "Set the date at today" },
			{ "main.button.today.mnemonic", new Character('y') },
			
			{ "main.button.today.RO", "Astazi" },
			{ "main.button.today.toolTip.RO", "Seteaza data ca fiind astazi" },
			{ "main.button.today.mnemonic.RO", new Character('z') },
			
			{ "main.date.day", "Day: " },
			{ "main.date.month", "Month: " },
			{ "main.date.year", "Year: " },
			{ "main.date.border", "Examination date" },
			{ "main.date.day.RO", "Ziua: " },
			{ "main.date.month.RO", "Luna: " },
			{ "main.date.year.RO", "Anul: " },
			{ "main.date.border.RO", "Data examinarii" },

			{ "main.button.save", "Save unit info" },
			{ "main.button.save.toolTip", "Save unit information" },
			{ "main.button.save.mnemonic", new Character('S') },
			{ "main.button.save.RO", "Salveaza" },
			{ "main.button.save.toolTip.RO", "Salveaza informatiile despre unitate" },
			{ "main.button.save.mnemonic.RO", new Character('S') },

			{ "main.button.load", "Load unit info" },
			{ "main.button.load.toolTip", "Load unit information" },
			{ "main.button.load.mnemonic", new Character('L') },
			{ "main.button.load.RO", "Incarca" },
			{ "main.button.load.toolTip.RO", "Incarca informatiile despre unitate" },
			{ "main.button.load.mnemonic.RO", new Character('I') },
			
			{ "main.button.add", "Add" },
			{ "main.button.add.toolTip", "Add to database" },
			{ "main.button.add.mnemonic", new Character('A') },
			{ "main.button.add.RO", "Adauga" },
			{ "main.button.add.toolTip.RO", "Adauga in baza de date" },
			{ "main.button.add.mnemonic.RO", new Character('A') },

			{ "main.button.delete", "Delete record" },
			{ "main.button.delete.toolTip", "Delete record" },
			{ "main.button.delete.mnemonic", new Character('D') },
			{ "main.button.delete.RO", "Sterge inregistrarea" },
			{ "main.button.delete.toolTip.RO", "Sterge inregistrarea" },
			{ "main.button.delete.mnemonic.RO", new Character('t') },

			{ "main.button.deleteAll", "Delete all records" },
			{ "main.button.deleteAll.toolTip", "Delete all records" },
			{ "main.button.deleteAll.mnemonic", new Character('e') },
			{ "main.button.deleteAll.RO", "Sterge toate inregistrarile" },
			{ "main.button.deleteAll.toolTip.RO", "Sterge toate inregistrarile" },
			{ "main.button.deleteAll.mnemonic.RO", new Character('e') },

			{ "main.button.report", "Generate report" },
			{ "main.button.report.toolTip",
					"Generate report to be saved in various format such as PDF, DOCX, XLS, ..." },
			{ "main.button.report.mnemonic", new Character('G') },
			{ "main.button.report.RO", "Genereaza raport" },
			{ "main.button.report.toolTip.RO",
					"Genereaza raport pentru a fi salvat in diferite formate cum ar fi: PDF, DOCX, XLS, ..." },
			{ "main.button.report.mnemonic.RO", new Character('G') },
			
			{ "main.showPlot", "Show plot" },
			{ "main.showPlot.RO", "Arata grafic" },
			
			{ "MainForm.sarcinaCb", new String[] { "Yes", "No" } },
			{ "MainForm.sarcinaCb.RO", new String[] { "Da", "Nu" } },
			{ "MainForm.gui.unitL", "Medical unit: " },{ "MainForm.gui.unitL.RO", "Unitate medicala: " },
			{ "MainForm.gui.adressL", "Address: " },{ "MainForm.gui.adressL.RO", "Adresa: " },
			{ "MainForm.gui.labL", "Contact: " },{ "MainForm.gui.labL.RO", "Contact: " },
			{ "MainForm.gui.deviceL", "X-ray device: " },{ "MainForm.gui.deviceL.RO", "Aparat X: " },
			{ "MainForm.gui.postL", "Notes: " },{ "MainForm.gui.postL.RO", "Note: " },
			{ "MainForm.gui.nameL", "Name: " },{ "MainForm.gui.nameL.RO", "Prenume: " },
			{ "MainForm.gui.surnameL", "Surname: " },{ "MainForm.gui.surnameL.RO", "Nume: " },
			{ "MainForm.gui.cnpL", "National Identification Number (NIN): " },{ "MainForm.gui.cnpL.RO", "Cod numeric personal (CNP): " },
			{ "MainForm.gui.examinareL", "Examination type: " },{ "MainForm.gui.examinareL.RO", "Tip examinare: " },
			{ "MainForm.gui.sarcinaL", "Pregnancy: " },{ "MainForm.gui.sarcinaL.RO", "Sarcina: " },
			{ "person.border", "Patient information" },// ///////////
			{ "person.border.RO", "Informatii pacient" },
			{ "MainForm.gui.campxL", "X-ray field width [cm]: " },{ "MainForm.gui.campxL.RO", "Dimensiune camp pe orizontala [cm]: " },
			{
					"MainForm.gui.camp.ToolTip",
					"If proper colimation and proper X field - light field alignment, this value is equal with the corresponding dimension on exposed radiological film!" },
			{
					"MainForm.gui.camp.ToolTip.RO",
					"Daca colimarea si aliniamentul campului X cu cel luminos sunt corespunzatoare, aceasta valoare poate fi masurata pe filmul radiologic!" },
				
			{ "MainForm.gui.campyL", "X-ray field height [cm]: " },{ "MainForm.gui.campyL.RO", "Dimensiune camp pe verticala [cm]: " },
			{ "MainForm.gui.fsdL", "Focus - X-ray field distance [cm]: " },
			{ "MainForm.gui.fsdL.RO", "Distanta focus-camp [cm]: " },
			{ "MainForm.gui.esdL", "Focus - patient entrance distance [cm]: " },
			{ "MainForm.gui.esdL.RO", "Distanta focus-suprafata de intrare in pacient [cm]: " },
			{ "camp.border", "X-field and distance information" },{ "camp.border.RO", "Informatii camp X si distante" },
			{ "MainForm.gui.uL", "Kilovoltage [kVp]: " },{ "MainForm.gui.uL.RO", "Kilovoltaj [kVp]: " },
			{ "MainForm.gui.iL", "Current [mA]: " },{ "MainForm.gui.iL.RO", "Curent [mA]: " },
			{ "MainForm.gui.tL", "Exposure time [ms]: " },{ "MainForm.gui.tL.RO", "Timp expunere [ms]: " },
			{ "MainForm.gui.itL", "mAs: " },
			{ "MainForm.gui.dapL", "KAP [uGy x m^2]: " },
			{ "param.border", "Tube parameters" },{ "param.border.RO", "Parametrii tub" },
			{ "unit.border", "Medical unit information" },// //////////////////
			{ "unit.border.RO", "Informatii unitate medicala" },
			{ "MainForm.gui.matanodL", "Anode material: " },{ "MainForm.gui.matanodL.RO", "Material anod: " },
			{ "MainForm.gui.uanodL", "Anode angle: " },{ "MainForm.gui.uanodL.RO", "Unghi anod: " },
			{ "MainForm.gui.rippleL", "Waveform ripple [%]: " },{ "MainForm.gui.rippleL.RO", "Ondulatia formei de unda [%]: " },
			{ "MainForm.gui.filtrareL", "Tube total filtration [mmAl]: " },{ "MainForm.gui.filtrareL.RO", "Filtrarea totala a tubului [mmAl]: " },
			{ "aux.border", "Auxiliary data for KAP evaluation" },
			{ "aux.border.RO", "Date auxiliare pentru evaluare KAP" },
			{ "MainForm.gui.contorL", "Records: " },//NA
			{ "MainForm.gui.dap.ToolTip",
					"If not given, it will be computed from auxiliary data!" },
			{ "MainForm.gui.dap.ToolTip.RO",
					"Daca nu este dat, va fi calculat din datele auxiliare!" },
					
			{ "MainForm.gui.mas.ToolTip",
					"If not given, it will be computed from mA and ms!" },
			{ "MainForm.gui.mas.ToolTip.RO",
					"Daca nu este dat, va fi calculat din mA si ms!" },
					
			{ "MainForm.gui.ma.ToolTip",
					"If not given, it will be computed from mAs and ms!" },
			{ "MainForm.gui.ma.ToolTip.RO",
					"Daca nu este dat, va fi calculat din mAs si ms!" },
					
			{
					"MainForm.gui.ripple.ToolTip",
					"Waveform ripple: CP (constant potential), ripple=0 %; 3phase12pulsuri, ripple~4 %; 3phase6pulsuri, ripple=~13 %!" },
			{
					"MainForm.gui.ripple.ToolTip.RO",
					"Ondulatie (ripple) forma de unda: CP (potential constant), ripple=0 %; 3phase12pulsuri, ripple~4 %; 3phase6pulsuri, ripple=~13 %!" },
						
			{
					"MainForm.gui.patient.ToolTip",
					"Usually, is equal with difference between focus-film distance (without Bucky) and patient thickness. Standard patient thickness: for AP/PA projection = 20cm and for LAT projection = 40cm." },
			{
					"MainForm.gui.patient.ToolTip.RO",
					"In general, este egal cu diferenta dintre distanta focus-film (fara Bucky) si grosimea pacientului. Grosimi pentru pacientul standard: pentru proiectii AP/PA = 20 cm si pentru proiectii LAT = 40 cm." },

			{ "dialog.delete.title", "Confirm delete" },
			{ "dialog.delete.message",
					"Are you sure?\n" + "All data will be erased!" },

			{
					"help.text",
					"The following parameters are required: tube kilovoltage kV, exposure time, tube current mA or tube load mAs, X field dimensions, focus-field distance, focus-patient entrance surface distance (usually, this is focus-field distance - patient thickness which is for standard patients of about 20 cm for AP,PA projections or 40 cm for LAT projections), and kerma area product KAP. "
							+ "\n"
							+ "If KAP is not measured by a DAPmeter, it can be evaluated using the following data: anode material, anode angle, total tube filtration and voltage waveform ripple. These data can be taken from device manual or from specific measurements (HVL, etc.). "
							+ "\n"
							+ "X field evaluation can be measured directly on exposed radiological film. If the film does not present unexposed margins then actual X field dimension cannot be inferred."
							+ "\n"
							+ "A better method in case of appropiate field alignment (X-light field alignment) is the measurement of light field dimensions on radiological table."
							+ "\n"
							+ "Focus field distance can therefore be the focus-film distance or focus-table distance."
							+ "\n"
							+ "Focus-patient entrance surface distance is the difference between the focus-field distance and all other distances form entrance surface and measured field such as patient thickness and Bucky distance (if appropiate)."
							+ "\n"
							+ "KAP = kerma x area = const. If electronic equilibrum (which is often the case for common Rx examinations) then kerma (free in air) equals dose (free in air)."
							+ "\n"
							+ "Dose = KAP/entrance surface area (at skin)."
							+ "\n" + "Dose rate= Dose/exposure time." },

			{
					"help.text.RO",
					"Pentru calcule, sunt necesare cunoasterea kV, timpului de expunere, mA sau mAs, campul, distanta focus-camp, distanta focus-suprafata de intrare (in general aceasta este distanta focus-camp - grosimea pacientului standard care este de aprox. 20 cm pentru proiectii AP,PA sau 40 cm pentru proiectii LAT), si indicatia KAP. "
							+ "\n"
							+ "Daca marimea KAP nu este masurata, ea poate fi evaluata cunoscand: materialul anodului, unghiul de inclinatie al anodului, filtrarea totala a tubului si ondulatia (ripple) kilovoltajului. Aceste date se pot afla consultand cartea tehnica a aparatului sau prin masuratori specifice (HVL, etc). "
							+ "\n"
							+ "Estimarea campului de raze X se poate face direct de pe film prin masurarea zonei EXPUSE. Daca intreg filmul este expus, atunci dimensiunea reala a campului nu poate fi cunoscuta."
							+ "\n"
							+ "O metoda mai buna, in cazul unui aliniament corespunzator al campului X cu cel luminos, ar fi masurarea campului luminos pe masa de radiografii."
							+ "\n"
							+ "Distanta focus - camp poate fi prin urmare distanta focus- film sau distanta focus - masa."
							+ "\n"
							+ "Distanta focus - suprafata de intrare in piele reprezinta distanta focus - camp din care se scad toate distantele implicate intre suprafata de intrare in pacient si locatia unde este masurat campul X cum ar fi: grosimea pacientului si distanta Bucky (daca este cazul)."
							+ "\n"
							+ "KAP = kerma x suprafata = constant. La echilibru electronic (care este adesea realizat pentru examinari Rx uzuale) kerma in aer este egala cu doza in aer."
							+ "\n"
							+ "Doza = DAP/suprafata la intrare in piele."
							+ "\n" + "Debitul dozei= Doza/timpul de expunere." },
							
			{ "report.title", "Patient report" },
			{ "report.title.RO", "Raportare pacienti" },
			{ "report.MU", "Medical unit: " },
			{ "report.MU.RO", "Unitate medicala: " },
			{ "report.address", "; Address: " },
			{ "report.address.RO", "; Adresa: " },
			{ "report.contact", "Contact: " },
			{ "report.contact.RO", "Contact: " },
			{ "report.device", "; Device: " },
			{ "report.device.RO", "; Aparat: " },
			{ "report.notes", "; Notes: " },
			{ "report.notes.RO", "; Note: " },
			
			{ "report.patient", "Surname, name" },
			{ "report.patient.RO", "Nume, prenume" },			
			{ "report.NIN", "NIN" },
			{ "report.NIN.RO", "CNP" },
			{ "report.exam", "Examination" },
			{ "report.exam.RO", "Examinare" },
			{ "report.pregnancy", "Pregnancy" },
			{ "report.pregnancy.RO", "Sarcina" },
			{ "report.yesno", "Yes, No" },
			{ "report.yesno.RO", "Da, Nu" },
			{ "report.field", "Field" },
			{ "report.field.RO", "Camp" },
			{ "report.distance1", "Focus-field" },
			{ "report.distance1.RO", "Distanta [cm]" },
			{ "report.distance2", "distance [cm]" },
			{ "report.distance2.RO", "focus-camp" },
			{ "report.KAP", "KAP or DAP" },
			{ "report.KAP.RO", "Indic. DAP" },
			{ "report.dose1", "Entrance" },
			{ "report.dose1.RO", "Doza [uGy]" },
			{ "report.dose2", "dose [uGy]" },
			{ "report.dose2.RO", "la intrare" },
			{ "report.doserate1", "Dose" },
			{ "report.doserate1.RO", "Debitul" },
			{ "report.doserate2", "rate [uGy/s]" },
			{ "report.doserate2.RO", "dozei [uGy/s]" },
			
			{ "report.no1", "No." },
			{ "report.no1.RO", "Nr." },
			{ "report.no2", "" },
			{ "report.no2.RO", "crt." },

	};
}
