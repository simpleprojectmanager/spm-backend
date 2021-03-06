# Simpleprojectmanager - Backend

### Fehler Handhabung
Wenn eine Anfrage nicht erwartungsgemäß bearbeitet werden kann, zb. wenn die Email oder das Passwort beim Einloggen falsch ist, wird folgender Body zurückgegeben.

```json
{
    "error":"Fehlercode"
}
```

``Fehlercode`` steht hierbei für einen beliebigen Fehlercode, welcher entweder in der ``globalen`` Sektion oder der Sektion der genutzen API aufgelistet ist.

### Globale Fehler

|Fehlercode|Beschreibung|
|----------|------------|
|``global.invalid``|Anfrage nicht ordnungsgemäß gestellt|

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
Die Tests müssen übersprungen werden, da diese leider nicht richtig funktionieren.

Sollte der Befehl nicht funktionieren, installieren Sie bitte die [Maven-CLI](https://maven.apache.org/download.cgi) und nutzen Sie anstelle von ``mvnw`` den Befehl ``mvn``
