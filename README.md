# Java PMP2_05

```
PR05 die Javanisten
Author: Roman Schmidt, Stanislaw Brug
```

### 1
Wir nutzen ein Array um eine Queue zu simulieren.  Dabei wandern die leeren Stellen innerhalb des Arrays. 
Um das zu realisieren nutzen wir einen "Pointer" _firstElement. Dieser zeigt uns wo das erste Element steht.
Zusätzlich dazu merken wir uns wiviele Elemente in der Queue drin sind. Mit hilfe dieser beiden Elemente und
Modulo sind wir in der Lage die aktuelle leere Position zu berechnen.

Beim Serialisieren und Deserialisieren merken wir uns neben der eigentlichen queue diesen Pointer, der maximalen
Länge auch die aktuelle Länge.

Es wurden mehrere Tests hinzugefügt, die sicherstellen, dass das Hinzufügen und entfernen der Elemente die
Pointer richtig verschiebt. Zusätzlich wurde das Serialisieren getestet, und damit sichergestellt, dass das
deserialisierte Objekt immer noch dem Original entspricht.

### 2
Wir erstellen eine neue super Klasse A von B. Diese hat eine Instanzvariable Long, die eine Instanz von
sich selbst hält. Damit haben wir Long für B überschrieben.

Wir überschreiben in der Klasse A die Funktion Compare, somit können wir sicherstellen, dass es keine
Division durch 0 gibt, da wir eine feste positive Zahl zurück geben.

Da B eine Instanz von B erstellt, erstellen wir in A eine statische Klasse B, damit es keine endlose Schleife
beim Initializieren gibt.

Die statische Klasse B muss nur noch den notwendigen Konstruktor mit einem Long implementieren, und schon sind
wir fertig.

Da B ein System.out hat, das wir prüfen müssten, verzichten wir auf Tests und erstellen eine main in der Klasse
Task5, damit man sich das selbst ansehen kann, dass es funktioniert. Wir verzichten auf das überschreiben
des std-Outputs.