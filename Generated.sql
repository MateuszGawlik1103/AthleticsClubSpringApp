/*
Created: 22.11.2023
Modified: 04.12.2023
Project: Klub_Lekkoatletyczny_BADA
Model: PM_klub
Company: Politechnika Warszawska
Author: Mateusz Gawlik, Adam Lipian
Database: Oracle 18c
*/


-- Create sequences section -------------------------------------------------

CREATE SEQUENCE Adresy_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 NOCACHE
/

CREATE SEQUENCE Nagrody_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 NOCACHE
/

CREATE SEQUENCE Terminy_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 NOCACHE
/

CREATE SEQUENCE Stanowisko_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 NOCACHE
/

CREATE SEQUENCE Poczty_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 NOCACHE
/

CREATE SEQUENCE Pracownicy_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Fizjo_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Dyscypliny_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Czlonkowie_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Grupy_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Wynagrodzenia_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE Zawody_seq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

-- Create tables section -------------------------------------------------

-- Table Adresy

CREATE TABLE Adresy(
  Nr_adresu Integer NOT NULL,
  Miasto Varchar2(20 ) NOT NULL,
  Ulica Varchar2(30 ) NOT NULL,
  Numer_lokalu Varchar2(5 ) NOT NULL,
  Nr_poczty Integer NOT NULL
)
/

-- Create indexes for table Adresy

CREATE INDEX IX_adres_ma_poczte ON Adresy (Nr_poczty)
/

-- Add keys for table Adresy

ALTER TABLE Adresy ADD CONSTRAINT PK_Adresy PRIMARY KEY (Nr_adresu)
/

-- Table and Columns comments section

COMMENT ON COLUMN Adresy.Nr_adresu IS 'Unikatowy identyfikator adresu'
/
COMMENT ON COLUMN Adresy.Miasto IS 'Miasto'
/
COMMENT ON COLUMN Adresy.Ulica IS 'Ulica'
/
COMMENT ON COLUMN Adresy.Numer_lokalu IS 'Numer lokalu'
/

-- Table Poczty

CREATE TABLE Poczty(
  Nr_poczty Integer NOT NULL,
  Kod_poczty Char(6 ) NOT NULL,
  Poczta Varchar2(20 ) NOT NULL
)
/

-- Add keys for table Poczty

ALTER TABLE Poczty ADD CONSTRAINT PK_Poczty PRIMARY KEY (Nr_poczty)
/

ALTER TABLE Poczty ADD CONSTRAINT Kod_poczty UNIQUE (Kod_poczty)
/

-- Table and Columns comments section

COMMENT ON COLUMN Poczty.Nr_poczty IS 'Unikalny identyfikator poczty'
/
COMMENT ON COLUMN Poczty.Kod_poczty IS 'Kod poczty w formacie XX-XXX'
/
COMMENT ON COLUMN Poczty.Poczta IS 'Lokalizacja poczty'
/

-- Table Stanowiska

CREATE TABLE Stanowiska(
  Nr_stanowiska Integer NOT NULL,
  Nazwa Varchar2(20 ) NOT NULL,
  Opis Varchar2(400 ) NOT NULL
)
/

-- Add keys for table Stanowiska

ALTER TABLE Stanowiska ADD CONSTRAINT PK_Stanowiska PRIMARY KEY (Nr_stanowiska)
/

ALTER TABLE Stanowiska ADD CONSTRAINT Nazwa UNIQUE (Nazwa)
/

-- Table and Columns comments section

COMMENT ON COLUMN Stanowiska.Nr_stanowiska IS 'Unikalny numer stanowiska'
/
COMMENT ON COLUMN Stanowiska.Nazwa IS 'Nazwa stanowiska'
/
COMMENT ON COLUMN Stanowiska.Opis IS 'Opis stanowiska'
/

-- Table Terminy

CREATE TABLE Terminy(
  Nr_terminu Integer NOT NULL,
  Dzien_tygodnia Varchar2(3 ) NOT NULL
        CHECK (Dzien_tygodnia IN ('PN','WT','SR','CZW','PT','SB','ND')),
  Godzina_od Varchar2(5 ) NOT NULL,
  Godzina_do Varchar2(5 ) NOT NULL
)
/

-- Add keys for table Terminy

ALTER TABLE Terminy ADD CONSTRAINT PK_Terminy PRIMARY KEY (Nr_terminu)
/

-- Table and Columns comments section

COMMENT ON COLUMN Terminy.Nr_terminu IS 'Numer terminu'
/
COMMENT ON COLUMN Terminy.Dzien_tygodnia IS 'Dzień tygodnia'
/
COMMENT ON COLUMN Terminy.Godzina_od IS 'Godzina rozpoczęcia w formacie XX:XX'
/
COMMENT ON COLUMN Terminy.Godzina_do IS 'Godzina zakończenia w formacie XX:XX'
/

-- Table Nagrody

CREATE TABLE Nagrody(
  Nr_nagrody Integer NOT NULL,
  I_miejsce Number(10,2) NOT NULL,
  II_miejsce Number(10,2) NOT NULL,
  III_miejsce Number(10,2) NOT NULL,
  Nr_zawodow Integer NOT NULL,
  Nr_dyscypliny Integer NOT NULL,
  Kat_wiekowa Varchar2(6 ) NOT NULL
        CHECK (Kat_wiekowa IN ('U15','U17','U19','U23','Senior'))
)
/

-- Create indexes for table Nagrody

CREATE INDEX IX_Nagrody_za_konkurencje ON Nagrody (Nr_zawodow,Nr_dyscypliny,Kat_wiekowa)
/

-- Add keys for table Nagrody

ALTER TABLE Nagrody ADD CONSTRAINT Nagrody_pk PRIMARY KEY (Nr_nagrody)
/

ALTER TABLE Nagrody ADD CONSTRAINT PK_Nagrody UNIQUE (Nr_nagrody)
/

-- Table and Columns comments section

COMMENT ON TABLE Nagrody IS 'Nagrody za zawody z poszczególnych dyscyplin'
/
COMMENT ON COLUMN Nagrody.Nr_nagrody IS 'Unikalny identyfikator nagrody'
/
COMMENT ON COLUMN Nagrody.I_miejsce IS 'Nagroda za I miesce'
/
COMMENT ON COLUMN Nagrody.II_miejsce IS 'Nagroda za II miejsce'
/
COMMENT ON COLUMN Nagrody.III_miejsce IS 'Nagroda za III miejsce'
/

-- Table Wynagrodzenia

CREATE TABLE Wynagrodzenia(
  Nr_wynagrodzenia Integer NOT NULL,
  Data_wynagrodzenia Date NOT NULL,
  Wynagrodzenie_brutto Number(10,2) NOT NULL,
  Nr_pracownika Integer NOT NULL
)
/

-- Create indexes for table Wynagrodzenia

CREATE INDEX XI_Pracownik_dostaje_wynagrodzenie ON Wynagrodzenia (Nr_pracownika)
/

-- Add keys for table Wynagrodzenia

ALTER TABLE Wynagrodzenia ADD CONSTRAINT PK_Wynagrodzenia PRIMARY KEY (Nr_wynagrodzenia)
/

-- Table and Columns comments section

COMMENT ON COLUMN Wynagrodzenia.Nr_wynagrodzenia IS 'Unikalny identyfikator numeru wynagrodzenia'
/
COMMENT ON COLUMN Wynagrodzenia.Data_wynagrodzenia IS 'Data ważności umowy'
/
COMMENT ON COLUMN Wynagrodzenia.Wynagrodzenie_brutto IS 'Wynagrodzenie_brutto'
/

-- Table Kluby_Lekkoatletyczne

CREATE TABLE Kluby_Lekkoatletyczne(
  Nr_klubu Integer NOT NULL,
  Nazwa Varchar2(30 ) NOT NULL,
  Data_zalozenia Date NOT NULL,
  Nr_adresu Integer NOT NULL
)
/

-- Create indexes for table Kluby_Lekkoatletyczne

CREATE INDEX IX_klub_ma_adres ON Kluby_Lekkoatletyczne (Nr_adresu)
/

-- Add keys for table Kluby_Lekkoatletyczne

ALTER TABLE Kluby_Lekkoatletyczne ADD CONSTRAINT Klub_lekkoatletyczny_PK PRIMARY KEY (Nr_klubu)
/

-- Table Pracownicy

CREATE TABLE Pracownicy(
  Nr_pracownika Integer NOT NULL,
  Imie Varchar2(30 ) NOT NULL,
  Nazwisko Varchar2(30 ) NOT NULL,
  Pesel Char(11 ),
  Data_urodzenia Date NOT NULL,
  Data_zatrudnienia Date NOT NULL,
  Data_zwolnienia Date,
  Numer_telefonu Varchar2(15 ) NOT NULL,
  Plec Char(1 ) NOT NULL
        CHECK (Plec IN ('K','M')),
  Nr_klubu Integer NOT NULL,
  Nr_adresu Integer NOT NULL,
  Nr_stanowiska Integer NOT NULL
)
/

-- Create indexes for table Pracownicy

CREATE INDEX IX_zatrudnia ON Pracownicy (Nr_klubu)
/

CREATE INDEX IX_pracownik_ma_adres ON Pracownicy (Nr_adresu)
/

CREATE INDEX IX_ma_stanowisko ON Pracownicy (Nr_stanowiska)
/

-- Add keys for table Pracownicy

ALTER TABLE Pracownicy ADD CONSTRAINT Pracownicy_PK PRIMARY KEY (Nr_pracownika)
/

-- Table Trenerzy

CREATE TABLE Trenerzy(
  Nr_pracownika Integer NOT NULL,
  Kwalifikacje Varchar2(20 ) NOT NULL
        CHECK (Kwalifikacje IN ('Trener I','Trener','Trener M','Instruktor')),
  Doswiadczenie_trenerskie Varchar2(400 ) NOT NULL
)
/

-- Add keys for table Trenerzy

ALTER TABLE Trenerzy ADD CONSTRAINT Unique_Identifier1 PRIMARY KEY (Nr_pracownika)
/

-- Table and Columns comments section

COMMENT ON COLUMN Trenerzy.Doswiadczenie_trenerskie IS 'Krótki opis doświadczenia trenera'
/

-- Table Fizjoterapeuci

CREATE TABLE Fizjoterapeuci(
  Nr_pracownika Integer NOT NULL,
  Specjalizacja Varchar2(30 ) NOT NULL,
  Kwalifikacje Varchar2(30 ) NOT NULL
)
/

-- Add keys for table Fizjoterapeuci

ALTER TABLE Fizjoterapeuci ADD CONSTRAINT Unique_Identifier2 PRIMARY KEY (Nr_pracownika)
/

-- Table Dyscypliny

CREATE TABLE Dyscypliny(
  Nr_dyscypliny Integer NOT NULL,
  Nazwa Varchar2(30 ) NOT NULL,
  Opis Varchar2(200 ) NOT NULL,
  Nr_klubu Integer NOT NULL
)
/

-- Create indexes for table Dyscypliny

CREATE INDEX IX_Prowadzi_dyscypline ON Dyscypliny (Nr_klubu)
/

-- Add keys for table Dyscypliny

ALTER TABLE Dyscypliny ADD CONSTRAINT Dyscyplina_PK PRIMARY KEY (Nr_dyscypliny)
/

ALTER TABLE Dyscypliny ADD CONSTRAINT Nazwa_dyscypliny UNIQUE (Nazwa)
/

-- Table Grupy

CREATE TABLE Grupy(
  Nr_grupy Integer NOT NULL,
  Cena_miesiac Number(10,2) NOT NULL,
  Maksymalna_liczba_czlonkow Integer NOT NULL,
  Kat_wiekowa Varchar2(6 ) NOT NULL
        CHECK (Kat_wiekowa IN ('U15','U17','U19','U23','Senior')),
  Nr_dyscypliny Integer NOT NULL,
  Nr_klubu Integer NOT NULL
)
/

-- Create indexes for table Grupy

CREATE INDEX IX_trenujaca ON Grupy (Nr_dyscypliny)
/

CREATE INDEX IX_Prowadzi_grupe ON Grupy (Nr_klubu)
/

-- Add keys for table Grupy

ALTER TABLE Grupy ADD CONSTRAINT Grupa_PK PRIMARY KEY (Nr_grupy)
/

-- Table Czlonkowie

CREATE TABLE Czlonkowie(
  Nr_czlonka Integer NOT NULL,
  Imie Varchar2(30 ) NOT NULL,
  Nazwisko Varchar2(30 ) NOT NULL,
  Data_zapisania Date NOT NULL,
  Data_wypisania Date,
  Data_urodzenia Date NOT NULL,
  Pesel Char(11 ),
  Plec Char(1 ) NOT NULL
        CHECK (Plec IN ('K','M')),
  Badania_lekarskie_waznosc Date NOT NULL,
  Nr_klubu Integer NOT NULL,
  Nr_adresu Integer NOT NULL
)
/

-- Create indexes for table Czlonkowie

CREATE INDEX IX_Ma_czlonka ON Czlonkowie (Nr_klubu)
/

CREATE INDEX IX_Czlonek_ma_adres ON Czlonkowie (Nr_adresu)
/

-- Add keys for table Czlonkowie

ALTER TABLE Czlonkowie ADD CONSTRAINT Czlonek_PK PRIMARY KEY (Nr_czlonka)
/

-- Table and Columns comments section

COMMENT ON COLUMN Czlonkowie.Pesel IS 'Pesel'
/
COMMENT ON COLUMN Czlonkowie.Plec IS 'Płeć'
/

-- Table Zawody

CREATE TABLE Zawody(
  Nr_zawodow Integer NOT NULL,
  Data Date NOT NULL,
  Opis Varchar2(300 ) NOT NULL,
  Nr_adresu Integer NOT NULL
)
/

-- Create indexes for table Zawody

CREATE INDEX IX_zawody_maja_adres ON Zawody (Nr_adresu)
/

-- Add keys for table Zawody

ALTER TABLE Zawody ADD CONSTRAINT Zawody_PK PRIMARY KEY (Nr_zawodow)
/

-- Table and Columns comments section

COMMENT ON COLUMN Zawody.Data IS 'Data zawodów'
/
COMMENT ON COLUMN Zawody.Opis IS 'Opis zawodów'
/

-- Table Uczestnicy_zawodow

CREATE TABLE Uczestnicy_zawodow(
  Nr_czlonka Integer NOT NULL,
  Nr_zawodow Integer NOT NULL,
  Nr_dyscypliny Integer NOT NULL,
  Kat_wiekowa Varchar2(6 ) NOT NULL
        CHECK (Kat_wiekowa IN ('U15','U17','U19','U23','Senior')),
  Wyniki Varchar2(30 ),
  Miejsce Integer
)
/

-- Create indexes for table Uczestnicy_zawodow

CREATE INDEX IX_biora_udzial ON Uczestnicy_zawodow (Nr_zawodow,Nr_dyscypliny,Kat_wiekowa)
/

-- Table and Columns comments section

COMMENT ON TABLE Uczestnicy_zawodow IS 'Uczestnik zawodów'
/
COMMENT ON COLUMN Uczestnicy_zawodow.Wyniki IS 'Wyniki z zawodów'
/
COMMENT ON COLUMN Uczestnicy_zawodow.Miejsce IS 'Miejsce w zawodach'
/

-- Table Terminy_zajec_grup

CREATE TABLE Terminy_zajec_grup(
  Nr_grupy Integer NOT NULL,
  Nr_terminu Integer NOT NULL
)
/

-- Add keys for table Terminy_zajec_grup

ALTER TABLE Terminy_zajec_grup ADD CONSTRAINT PK_Terminy_zajec_grup PRIMARY KEY (Nr_grupy,Nr_terminu)
/

-- Table Czlonkowie_grup

CREATE TABLE Czlonkowie_grup(
  Nr_czlonka Integer NOT NULL,
  Nr_grupy Integer NOT NULL
)
/

-- Add keys for table Czlonkowie_grup

ALTER TABLE Czlonkowie_grup ADD CONSTRAINT PK_Czlonkowie_grup PRIMARY KEY (Nr_czlonka,Nr_grupy)
/

-- Table Trenerzy_grup

CREATE TABLE Trenerzy_grup(
  Nr_pracownika Integer NOT NULL,
  Nr_grupy Integer NOT NULL
)
/

-- Add keys for table Trenerzy_grup

ALTER TABLE Trenerzy_grup ADD CONSTRAINT PK_Trenerzy_grup PRIMARY KEY (Nr_pracownika,Nr_grupy)
/

-- Table Konkurencje_na_zawodach

CREATE TABLE Konkurencje_na_zawodach(
  Nr_zawodow Integer NOT NULL,
  Nr_dyscypliny Integer NOT NULL,
  Kat_wiekowa Varchar2(6 ) NOT NULL
        CONSTRAINT Kat_wiekowaCR CHECK (Kat_wiekowa IN ('U15','U17','U19','U23','Senior'))
        CHECK (Kat_wiekowa IN ('U15','U17','U19','U23','Senior'))
)
/

-- Add keys for table Konkurencje_na_zawodach

ALTER TABLE Konkurencje_na_zawodach ADD CONSTRAINT PK_Konkurencje_na_zawodach PRIMARY KEY (Nr_zawodow,Nr_dyscypliny,Kat_wiekowa)
/

-- Trigger for sequence Pracownicy_seq1 for column Nr_pracownika in table Pracownicy ---------
CREATE OR REPLACE TRIGGER ts_Pracownicy_Pracownicy_seq1 BEFORE INSERT
ON Pracownicy FOR EACH ROW
BEGIN
  :new.Nr_pracownika := Pracownicy_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Pracownicy_Pracownicy_seq1 AFTER UPDATE OF Nr_pracownika
ON Pracownicy FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_pracownika in table Pracownicy as it uses sequence.');
END;
/

-- Trigger for sequence Dyscypliny_seq1 for column Nr_dyscypliny in table Dyscypliny ---------
CREATE OR REPLACE TRIGGER ts_Dyscypliny_Dyscypliny_seq1 BEFORE INSERT
ON Dyscypliny FOR EACH ROW
BEGIN
  :new.Nr_dyscypliny := Dyscypliny_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Dyscypliny_Dyscypliny_seq1 AFTER UPDATE OF Nr_dyscypliny
ON Dyscypliny FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_dyscypliny in table Dyscypliny as it uses sequence.');
END;
/

-- Trigger for sequence Grupy_seq1 for column Nr_grupy in table Grupy ---------
CREATE OR REPLACE TRIGGER ts_Grupy_Grupy_seq1 BEFORE INSERT
ON Grupy FOR EACH ROW
BEGIN
  :new.Nr_grupy := Grupy_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Grupy_Grupy_seq1 AFTER UPDATE OF Nr_grupy
ON Grupy FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_grupy in table Grupy as it uses sequence.');
END;
/

-- Trigger for sequence Czlonkowie_seq1 for column Nr_czlonka in table Czlonkowie ---------
CREATE OR REPLACE TRIGGER ts_Czlonkowie_Czlonkowie_seq1 BEFORE INSERT
ON Czlonkowie FOR EACH ROW
BEGIN
  :new.Nr_czlonka := Czlonkowie_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Czlonkowie_Czlonkowie_seq1 AFTER UPDATE OF Nr_czlonka
ON Czlonkowie FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_czlonka in table Czlonkowie as it uses sequence.');
END;
/

-- Trigger for sequence Zawody_seq1 for column Nr_zawodow in table Zawody ---------
CREATE OR REPLACE TRIGGER ts_Zawody_Zawody_seq1 BEFORE INSERT
ON Zawody FOR EACH ROW
BEGIN
  :new.Nr_zawodow := Zawody_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Zawody_Zawody_seq1 AFTER UPDATE OF Nr_zawodow
ON Zawody FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_zawodow in table Zawody as it uses sequence.');
END;
/

-- Trigger for sequence Adresy_seq1 for column Nr_adresu in table Adresy ---------
CREATE OR REPLACE TRIGGER ts_Adresy_Adresy_seq1 BEFORE INSERT
ON Adresy FOR EACH ROW
BEGIN
  :new.Nr_adresu := Adresy_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Adresy_Adresy_seq1 AFTER UPDATE OF Nr_adresu
ON Adresy FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_adresu in table Adresy as it uses sequence.');
END;
/

-- Trigger for sequence Stanowisko_seq1 for column Nr_stanowiska in table Stanowiska ---------
CREATE OR REPLACE TRIGGER ts_Stanowiska_Stanowisko_seq1 BEFORE INSERT
ON Stanowiska FOR EACH ROW
BEGIN
  :new.Nr_stanowiska := Stanowisko_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Stanowiska_Stanowisko_seq1 AFTER UPDATE OF Nr_stanowiska
ON Stanowiska FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_stanowiska in table Stanowiska as it uses sequence.');
END;
/

-- Trigger for sequence Wynagrodzenia_seq1 for column Nr_wynagrodzenia in table Wynagrodzenia ---------
CREATE OR REPLACE TRIGGER ts_Wynagrodzenia_Wynagrodzenia_seq1 BEFORE INSERT
ON Wynagrodzenia FOR EACH ROW
BEGIN
  :new.Nr_wynagrodzenia := Wynagrodzenia_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Wynagrodzenia_Wynagrodzenia_seq1 AFTER UPDATE OF Nr_wynagrodzenia
ON Wynagrodzenia FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_wynagrodzenia in table Wynagrodzenia as it uses sequence.');
END;
/

-- Trigger for sequence Terminy_seq1 for column Nr_terminu in table Terminy ---------
CREATE OR REPLACE TRIGGER ts_Terminy_Terminy_seq1 BEFORE INSERT
ON Terminy FOR EACH ROW
BEGIN
  :new.Nr_terminu := Terminy_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Terminy_Terminy_seq1 AFTER UPDATE OF Nr_terminu
ON Terminy FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_terminu in table Terminy as it uses sequence.');
END;
/

-- Trigger for sequence Nagrody_seq1 for column Nr_nagrody in table Nagrody ---------
CREATE OR REPLACE TRIGGER ts_Nagrody_Nagrody_seq1 BEFORE INSERT
ON Nagrody FOR EACH ROW
BEGIN
  :new.Nr_nagrody := Nagrody_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Nagrody_Nagrody_seq1 AFTER UPDATE OF Nr_nagrody
ON Nagrody FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_nagrody in table Nagrody as it uses sequence.');
END;
/

-- Trigger for sequence Poczty_seq1 for column Nr_poczty in table Poczty ---------
CREATE OR REPLACE TRIGGER ts_Poczty_Poczty_seq1 BEFORE INSERT
ON Poczty FOR EACH ROW
BEGIN
  :new.Nr_poczty := Poczty_seq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Poczty_Poczty_seq1 AFTER UPDATE OF Nr_poczty
ON Poczty FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_poczty in table Poczty as it uses sequence.');
END;
/


-- Create foreign keys (relationships) section ------------------------------------------------- 

ALTER TABLE Pracownicy ADD CONSTRAINT Zatrudnia FOREIGN KEY (Nr_klubu) REFERENCES Kluby_Lekkoatletyczne (Nr_klubu)
/



ALTER TABLE Grupy ADD CONSTRAINT Trenujaca FOREIGN KEY (Nr_dyscypliny) REFERENCES Dyscypliny (Nr_dyscypliny)
/



ALTER TABLE Grupy ADD CONSTRAINT Prowadzi_grupe FOREIGN KEY (Nr_klubu) REFERENCES Kluby_Lekkoatletyczne (Nr_klubu)
/



ALTER TABLE Czlonkowie ADD CONSTRAINT Ma_czlonka FOREIGN KEY (Nr_klubu) REFERENCES Kluby_Lekkoatletyczne (Nr_klubu)
/



ALTER TABLE Dyscypliny ADD CONSTRAINT Prowadzi_dyscypline FOREIGN KEY (Nr_klubu) REFERENCES Kluby_Lekkoatletyczne (Nr_klubu)
/



ALTER TABLE Kluby_Lekkoatletyczne ADD CONSTRAINT Klub_ma_adres FOREIGN KEY (Nr_adresu) REFERENCES Adresy (Nr_adresu)
/



ALTER TABLE Pracownicy ADD CONSTRAINT Pracownik_ma_adres FOREIGN KEY (Nr_adresu) REFERENCES Adresy (Nr_adresu)
/



ALTER TABLE Czlonkowie ADD CONSTRAINT Czlonek_ma_adres FOREIGN KEY (Nr_adresu) REFERENCES Adresy (Nr_adresu)
/



ALTER TABLE Wynagrodzenia ADD CONSTRAINT Pracownik_ma_wynagrodzenie FOREIGN KEY (Nr_pracownika) REFERENCES Pracownicy (Nr_pracownika)
/



ALTER TABLE Adresy ADD CONSTRAINT Adres_ma_poczte FOREIGN KEY (Nr_poczty) REFERENCES Poczty (Nr_poczty)
/



ALTER TABLE Zawody ADD CONSTRAINT Zawody_maja_adres FOREIGN KEY (Nr_adresu) REFERENCES Adresy (Nr_adresu)
/



ALTER TABLE Pracownicy ADD CONSTRAINT Pracownik_ma_stanowisko FOREIGN KEY (Nr_stanowiska) REFERENCES Stanowiska (Nr_stanowiska)
/



ALTER TABLE Terminy_zajec_grup ADD CONSTRAINT Grupy_maja_zajecia FOREIGN KEY (Nr_grupy) REFERENCES Grupy (Nr_grupy)
/



ALTER TABLE Terminy_zajec_grup ADD CONSTRAINT Zajecia_maja_terminy FOREIGN KEY (Nr_terminu) REFERENCES Terminy (Nr_terminu)
/



ALTER TABLE Czlonkowie_grup ADD CONSTRAINT Uczeszczaja_do FOREIGN KEY (Nr_czlonka) REFERENCES Czlonkowie (Nr_czlonka)
/



ALTER TABLE Czlonkowie_grup ADD CONSTRAINT Grupa_zajmowana_przez FOREIGN KEY (Nr_grupy) REFERENCES Grupy (Nr_grupy)
/



ALTER TABLE Trenerzy_grup ADD CONSTRAINT Pracuje_z_trener FOREIGN KEY (Nr_pracownika) REFERENCES Trenerzy (Nr_pracownika)
/



ALTER TABLE Trenerzy_grup ADD CONSTRAINT Pracuje_z_grupa FOREIGN KEY (Nr_grupy) REFERENCES Grupy (Nr_grupy)
/



ALTER TABLE Konkurencje_na_zawodach ADD CONSTRAINT Zawody_maja_konkurencje FOREIGN KEY (Nr_zawodow) REFERENCES Zawody (Nr_zawodow)
/



ALTER TABLE Konkurencje_na_zawodach ADD CONSTRAINT Dotyczace FOREIGN KEY (Nr_dyscypliny) REFERENCES Dyscypliny (Nr_dyscypliny)
/



ALTER TABLE Nagrody ADD CONSTRAINT Konkurencje_maja_nagrody FOREIGN KEY (Nr_zawodow, Nr_dyscypliny, Kat_wiekowa) REFERENCES Konkurencje_na_zawodach (Nr_zawodow, Nr_dyscypliny, Kat_wiekowa)
/



ALTER TABLE Uczestnicy_zawodow ADD CONSTRAINT Zawody_maja_uczestnikow FOREIGN KEY (Nr_zawodow, Nr_dyscypliny, Kat_wiekowa) REFERENCES Konkurencje_na_zawodach (Nr_zawodow, Nr_dyscypliny, Kat_wiekowa)
/





