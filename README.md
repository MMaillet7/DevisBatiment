# Devis Batiment

Application **JavaFX** de gestion de devis de batiment,
organisee selon le patron de conception **MVC (Model - View - Controller)**,
sans FXML (vues construites entierement en Java).

---

## Sommaire

- [Pre-requis](#pre-requis)
- [Installer Maven](#installer-maven)
- [Lancer le projet](#lancer-le-projet)
- [Architecture MVC](#architecture-mvc)
- [Format du catalogue](#format-du-catalogue)
- [Persistance](#persistance)
- [A quoi servent les dossiers test et resources ?](#a-quoi-servent-les-dossiers-test-et-resources)

---

## Pre-requis

- **JDK 17.0.11** — verifier avec `java -version`
- **Maven 3.8+** — voir section suivante

---

## Installer Maven

**1. Telecharger Maven**

Aller sur https://maven.apache.org/download.cgi et telecharger le fichier
**Binary zip archive** : `apache-maven-3.9.x-bin.zip`

**2. Extraire l'archive**

Dezipper dans un dossier stable, par exemple :
```
C:\Program Files\Maven\apache-maven-3.9.x
```

**3. Ajouter Maven au PATH (Windows)**

- Appuyer sur `Windows + R`, taper `sysdm.cpl`, valider
- Aller dans l'onglet **Avance** -> **Variables d'environnement**
- Dans **Variables systeme**, selectionner `Path` -> **Modifier**
- Cliquer **Nouveau** et ajouter :
```
C:\Program Files\Maven\apache-maven-3.9.x\bin
```
- Valider sur toutes les fenetres

**4. Verifier l'installation**

Fermer et rouvrir VS Code, puis dans le terminal :
```powershell
mvn -v
```
Resultat attendu :
```
Apache Maven 3.9.x
Java version: 17.x.x
```

---

## Lancer le projet

Ouvrir un terminal dans le dossier `devis-batiment` (celui qui contient `pom.xml`) :

```powershell
mvn clean javafx:run
```

- **Premier lancement** : Maven telecharge automatiquement toutes les dependances
  (JavaFX, Jackson, PDFBox) depuis internet. Cela prend 1 a 2 minutes.
- **Lancements suivants** : tout est en cache dans `C:\Users\VotreNom\.m2\`,
  le lancement est quasi instantane.

Pour executer les tests unitaires :
```powershell
mvn test
```

---

## Architecture MVC

```
src/main/java/com/devis/batiment/
+-- Main.java                        <-- point d'entree
+-- model/                           <-- donnees metier (aucune dependance JavaFX)
|   +-- enums/
|   |   +-- StatutDevis.java
|   |   +-- TypeBatiment.java
|   |   +-- TypeOuverture.java
|   +-- Batiment.java                (classe abstraite)
|   +-- Maison.java
|   +-- Immeuble.java
|   +-- Niveau.java
|   +-- Appartement.java
|   +-- Piece.java
|   +-- Mur.java
|   +-- Coin.java
|   +-- Ouverture.java               (abstraite : Porte, Fenetre)
|   +-- EntreeRevetement.java
|   +-- Projet.java                  (racine serialisee en JSON)
+-- view/                            <-- interface JavaFX (0 FXML)
|   +-- MainView.java
|   +-- AccueilView.java
|   +-- PlanView.java
|   +-- CatalogueView.java
+-- controller/                      <-- evenements et glue View <-> Service
|   +-- MainController.java
|   +-- AccueilController.java
|   +-- PlanController.java
|   +-- CatalogueController.java
+-- service/                         <-- logique applicative
|   +-- ProjetService.java
|   +-- CatalogueService.java
|   +-- PlanService.java
|   +-- ExportService.java
+-- repository/                      <-- persistance
|   +-- ProjetRepository.java        (JSON via Jackson)
|   +-- CatalogueRepository.java     (lecture CSV .txt)
+-- dessin/                          <-- moteur vectoriel 2D
|   +-- Figure.java, FigureSimple.java, Groupe.java
|   +-- PointDessin.java, Segment.java
|   +-- RectangleHV.java, PlanCanvas.java
+-- util/
    +-- AlertUtil.java
    +-- FormatUtil.java
```

---

## Format du catalogue

Fichier `.txt` avec separateur `;` :
```
idRevetement;designation;pourMur(0/1);pourSol(0/1);pourPlafond(0/1);prixUnitaire
1;Peinture;1;0;1;10.95
2;Carrelage;1;1;0;49.75
13;Parquet;0;1;0;46.36
```

Les menus deroulants de revetement filtrent automatiquement les entrees
selon leur applicabilite (mur, sol ou plafond).

---

## Persistance

Les projets sont sauvegardes en **JSON** (`.json`) via Jackson.
Le chemin du catalogue est memorise dans le JSON et rechargé automatiquement
a l'ouverture du projet.

---

## A quoi servent les dossiers test et resources ?

### `src/test/`

Ce dossier contient les **tests automatises** du projet. Un test automatise est
un programme qui verifie qu'une fonctionnalite du code produit bien le resultat
attendu, sans intervention humaine.

```
src/test/java/com/devis/batiment/service/
+-- DevisServiceTest.java   <-- teste les calculs de surfaces et de couts
```

Pour lancer les tests : `mvn test`

Maven affiche ensuite un rapport indiquant combien de tests ont reussi ou echoue.
Ces tests permettent de s'assurer que les calculs (surface d'un mur, cout d'une
piece, devis total) restent corrects apres chaque modification du code.

### `src/main/resources/`

Ce dossier contient les **fichiers non-Java** necessaires a l'application :
images, feuilles de style CSS, fichiers de configuration, etc.

```
src/main/resources/com/devis/batiment/
+-- css/        <-- feuilles de style JavaFX (fichiers .css)
+-- images/     <-- icones et images de l'interface
```

Ces fichiers sont copies automatiquement dans le JAR final lors du build Maven.
Dans le code Java, on y accede via `getClass().getResource("/com/devis/batiment/css/style.css")`.
Pour ce projet, le dossier est present mais vide car les styles sont appliques
directement en Java (via `setStyle(...)`).
