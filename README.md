

# haushaltsplan-backend
Backend für das [private Haushaltsplaner-Tool](https://github.com/janetschel/haushaltsplan)

Das ist die Implementation für das Backend - geschrieben in Spring-Boot mit einer Connection zu einer gehosteten MongoDB fürs Data-Storing.

Gesichert ist das Backend durch ein Token-System. Der Token ist erst nach gelungener Anmeldung im Frontend erhätlich und garantiert somit, dass nur User mit den entsprechenden Berechtigungen Datenbestände der Datenbank erstellen/ändern/löschen können.

Der einzige Path, welcher nach außen sichtbar und ohne Token o.Ä. accessible ist, ist der Healthcheck.

## Status vom Backend

[![Heroku App Status](http://heroku-shields.herokuapp.com/haushaltsplan)](https://haushaltsplan-backend.herokuapp.com/healthcheck)
[![Deployment Status](https://img.shields.io/github/deployments/janetschel/haushaltsplan-backend/haushaltsplan-backend?label=state%20of%20deployment)](https://haushaltsplan-backend.herokuapp.com/healthcheck)

