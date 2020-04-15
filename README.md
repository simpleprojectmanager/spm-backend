# Simpleprojectmanager - Backend

### Fehler Handhabung
Wenn eine Anfrage nicht erwartungsgemäß bearbeitet werden kann, zb. wenn die Email oder das Passwort beim Einloggen falsch ist, wird folgender Body zurückgegeben.

```json
{
    "error":"Fehlercode"
}
```

``Fehlercode`` steht hierbei für einen beliebigen Fehlercode welcher entweder in der ``globalen`` Sektion oder der Sektion der genutzen API aufgelistet ist.

### Globale Fehler

|Fehlercode|Beschreibung|
|----------|------------|
|``global.invalid``|Anfrage nicht ordnungsgemäß gestellt|

### Benutzer erstellung

Bisher keine API-Methode

|Fehlercode|Beschreibung|
|----------|------------|
|``user.create.invalid.password``|Invalides Passwort (min.8,1 Sonderzeichen)|
|``user.create.invalid.email ``|Invalide Email (max.40)|
|``user.create.invalid.firstname``|Invalider Vorname (min.2,max.20)|
|``user.create.invalid.lastname``|Invalider Nachname (min.2,max 20)|
|``user.create.invalid.nickname``|Invalider Nickname (min.4,max 20)|
|``user.create.failed``|Unbekannter Fehler|
|``user.create.used.email``|Email bereits genutzt|
|``user.create.used.nickname``|Nickname bereits genutzt|

* Passwort:
  * Mindestlänge von 8
  * Maximallänge von 20
  * Mindestens ein Sonderzeichen muss vorhanden sein (Kein Whitespace).
  * Regex zur Überprüfung im Backend: ``^(?=.*?[^\w].*?).{8,20}$``
* Email:
  * Maximallänge von 40
  * Darf nicht für mehrere Nutzer verwendet werden
  * Regex zur Überprüfung im Backend: ``^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$``
* Vorname:
  * Mindestlänge von 2
  * Maximallänge von 20
  * Regex zum Überprüfen im Backend: ``^\S{2,20}$``
* Nachname:
  * Mindestlänge von 2
  * Maximallänge von 20
  * Regex zum Überprüfen im Backend: ``^\S{2,20}$``
* Nickname:
  * Mindestlänge von 4
  * Maximallänge von 40
  * Regex zum Überprüfen im Backend: ``^\S{4,40}$``
  
### Anmeldung

``POST`` Anfrage an ``api/login``

|Fehlercode|Beschreibung|
|----------|------------|
|``user.login.failed``|Login Fehlgeschlagen|

Die Login-API erwartet folgende Anfrageparameter (Beispieldaten):

```json
{
    "email":"m.muster@firma.de",
    "password":"1337mustermax42?0"
}
```

Bei erfolgreicher Anmeldungen werden folgende Daten zurückgegeben (Beispielsdaten):

```json
{
  "nickname": "m.muster",
  "firstname": "max",
  "lastname": "musterman",
  "csrftoken": "(7dhfz&foI",
  "sessiontoken": "plo!OY(^,Wi9db,UoDD#ir0@!Evdh5"
}
```

# Exportieren
Mit der CLI den folgenden Befehl ausführen.
```shell script
mvnw -Dmaven.test.skip package
```

Damit werden alle Testes übersprungen und automatisch die ``jar`` in den Ordner ``target/`` abgelegt.
Die Tests müssen übersprungen werden, da diese leider nicht richtig Funktionieren.

Sollte der Befehlt nicht funktionierten, installieren Sie bitte die [Maven-CLI](https://maven.apache.org/download.cgi) und nutzen Sie anstelle von ``mvnw`` den Befehl ``mvn``