# 🏎️ Crazy Roads

Un jeu de course automobile en Java avec JavaFX proposant deux modes de jeu distincts : un mode infini inspiré de Subway Surfers et un mode contre-la-montre inspiré de Mario Kart.

## 📋 Description

Crazy Roads est un jeu de course où le joueur doit éviter des obstacles tout en progressant sur une route défilante. Le jeu propose deux expériences de jeu différentes avec système de scoring, classement et sauvegarde automatique des scores.

## 🎮 Modes de Jeu

### Mode Infini (Subway Surfers)
- Course sans fin avec difficulté progressive
- 3 vies au départ
- Score basé sur la distance parcourue
- Augmentation progressive de la vitesse
- Objectif : Obtenir le meilleur score possible

### Mode Mario Kart (Contre-la-Montre)
- Course chronométrée sur 40 frames
- 3 vies au départ
- Système de boost avec la touche ESPACE
- Ligne d'arrivée visible à la fin du parcours
- Objectif : Terminer le parcours le plus rapidement possible

## 🎯 Fonctionnalités

### Système de Boost (Mode Mario Kart)
- **Activation** : Touche ESPACE
- **Durée** : 5 secondes
- **Effet** : Vitesse x2
- **Cooldown** : 3 secondes entre chaque boost
- Indicateur visuel dans le HUD

### Système de Scoring
- Sauvegarde automatique de **tous les scores** après chaque partie
- Affichage des 5 meilleurs scores par mode
- Format d'affichage :
  - Mode Infini : Points (score le plus élevé)
  - Mode Contre-la-Montre : Temps au format MM.SS minutes (temps le plus rapide)
- Fichier de sauvegarde : `scores.dat`

### Interface
- HUD affichant :
  - Temps écoulé / Score
  - Nombre de vies restantes
  - Vitesse actuelle
  - Indicateur de boost avec temps restant (Mode Mario Kart)
  - Indicateur de cooldown du boost (Mode Mario Kart)
- Menu pause (touche P)
- Écran de game over avec statistiques et indication de nouveau record
- Système de classement accessible depuis le menu principal

## 🕹️ Contrôles

### Contrôles par Défaut

| Touche | Action |
|--------|--------|
| ← (GAUCHE) ou Q | Déplacer la voiture à gauche |
| → (DROITE) ou D | Déplacer la voiture à droite |
| ESPACE | Activer le boost (Mode Mario Kart uniquement) |
| P | Mettre en pause / Reprendre |
| ESC | Retour au menu (depuis la pause) |

### Personnalisation des Touches

Les touches peuvent être personnalisées via le fichier `settings.ini` :
```ini
leftKey=Q
rightKey=D
pauseKey=P
boostKey=SPACE
```

## 🛠️ Prérequis

- **Java** : JDK 17 ou supérieur
- **Maven** : 3.6+ (inclus via Maven Wrapper)
- **JavaFX** : 21.0.6 (géré automatiquement par Maven)

## 🚀 Installation et Lancement

### Avec Maven Wrapper (Recommandé)

#### Windows
```cmd
mvnw.cmd clean compile javafx:run
```

#### Linux/Mac
```bash
./mvnw clean compile javafx:run
```

### Avec Maven Installé
```bash
mvn clean compile javafx:run
```

## 🏗️ Structure du Projet

```
Crazy_Roads/
├── src/
│   ├── main/
│   │   ├── java/com/example/crazy_roads/
│   │   │   ├── Main.java                    # Point d'entrée
│   │   │   ├── generation/
│   │   │   │   └── GenerationProcedurale.java
│   │   │   ├── gui/
│   │   │   │   ├── GameStateManager.java
│   │   │   │   ├── HUD.java
│   │   │   │   ├── IGamePage.java           # Interface commune aux pages de jeu
│   │   │   │   ├── MenuGameOver.java
│   │   │   │   ├── MenuMeilleursScores.java
│   │   │   │   ├── MenuOptions.java
│   │   │   │   ├── MenuPause.java           # Menu pause universel
│   │   │   │   ├── PageAccueil.java
│   │   │   │   ├── PageJeuMarioKart.java
│   │   │   │   └── PageJeuSubwaySurfers.java
│   │   │   ├── managers/
│   │   │   │   ├── MusicManager.java
│   │   │   │   ├── RessourceLoader.java
│   │   │   │   ├── ScoreManager.java        # Gestion automatique des scores
│   │   │   │   └── Settings.java            # Configuration et touches
│   │   │   ├── models/
│   │   │   │   ├── Frame.java
│   │   │   │   ├── Obstacle.java
│   │   │   │   └── Voiture.java             # Boost avec cooldown
│   │   │   └── modes/
│   │   │       ├── ModeMarioKart.java
│   │   │       └── ModeSubwaySurfers.java
│   │   └── resources/
│   │       ├── audios/
│   │       ├── images/
│   │       │   ├── Carte/
│   │       │   │   ├── Frame1.png           # Frames de route standard
│   │       │   │   └── Finish line.png      # Ligne d'arrivée
│   │       │   └── voiture/
│   │       │       └── obstacles/
│   │       └── sounds/
│   └── test/
│       └── java/com/example/crazy_roads/
│           ├── generation/
│           ├── managers/
│           └── models/
├── pom.xml
├── scores.dat                               # Fichier de sauvegarde des scores
└── settings.ini                             # Configuration personnalisée

Document/
├── README.md                                # Documentation du projet
├── javadoc/                                 # Documentation Javadoc générée
└── coverage/                                # Rapport de couverture des tests
```

## 🧪 Tests

Le projet utilise JUnit 5 pour les tests unitaires avec couverture de code via JaCoCo.

### Lancer les tests
```bash
# Windows
mvnw.cmd test

# Linux/Mac
./mvnw test

# Avec Maven installé
mvn test
```

### Rapport de couverture
```bash
# Windows
mvnw.cmd jacoco:report

# Linux/Mac
./mvnw jacoco:report

# Avec Maven installé
mvn jacoco:report
```
Le rapport sera généré dans `../Document/coverage/index.html`

## 📚 Documentation Javadoc

Le projet inclut une documentation complète de toutes les classes et méthodes.

### Générer la Javadoc
```bash
# Windows
mvnw.cmd javadoc:javadoc

# Linux/Mac
./mvnw javadoc:javadoc

# Avec Maven installé
mvn javadoc:javadoc
```

La documentation sera générée dans `../Document/javadoc/index.html`

### Consulter la Javadoc
Une fois générée, ouvrez le fichier suivant dans votre navigateur :
- **Windows** : `..\Document\javadoc\index.html`
- **Linux/Mac** : `../Document/javadoc/index.html`

Ou utilisez la commande :
```powershell
# Windows PowerShell
Start-Process "..\Document\javadoc\index.html"
```

```bash
# Linux/Mac
open ../Document/javadoc/index.html
```

## 📊 Gestion des Scores

Les scores sont sauvegardés automatiquement dans le fichier `scores.dat` au format :
```
COURSE_INFINIE=52
CONTRE_LA_MONTRE=45
```

- **COURSE_INFINIE** : Score en points (plus élevé = meilleur)
- **CONTRE_LA_MONTRE** : Temps en secondes (plus bas = meilleur)

## ⚙️ Configuration

Le fichier `settings.ini` permet de personnaliser :
- **Volume de la musique** : `musicVolume` (0.0 à 1.0)
- **Volume des effets sonores** : `soundVolume` (0.0 à 1.0)
- **Résolution d'écran** : `screenWidth` et `screenHeight`
- **Touches de contrôle** :
  - `leftKey` : Déplacement gauche (par défaut: Q)
  - `rightKey` : Déplacement droite (par défaut: D)
  - `pauseKey` : Pause (par défaut: P)
  - `boostKey` : Boost (par défaut: SPACE)

### Exemple de settings.ini
```ini
# Paramètres du jeu Crazy Roads
musicVolume=0.5
soundVolume=0.5
screenWidth=1920.0
screenHeight=1080.0
leftKey=Q
rightKey=D
pauseKey=P
boostKey=SPACE
```

## 🎨 Assets

Le jeu utilise des ressources graphiques et sonores stockées dans `src/main/resources/` :
- **Images** : 
  - Sprites de voiture
  - Obstacles (barils bleus et rouges)
  - Frames de route standard (`Frame1.png`)
  - Ligne d'arrivée pour le mode Mario Kart (`Finish line.png`)
- **Sons** : Musiques de fond et effets sonores

## 🐛 Débogage

### Logs
Le projet a été nettoyé de tous les logs de debug pour la production. En cas de problème :
- Vérifier que `scores.dat` existe et est accessible en écriture
- Vérifier que `settings.ini` contient des valeurs valides
- S'assurer que Java 17+ est installé

### Problèmes Courants
- **Le jeu ne se lance pas** : Vérifier JAVA_HOME et Maven
- **Les scores ne se sauvent pas** : Vérifier les permissions du fichier `scores.dat`
- **La touche pause ne fonctionne pas** : Vérifier `pauseKey=P` dans `settings.ini`

## 🚀 Améliorations Récentes

### Version Mario Kart
- ✅ Mode contre-la-montre avec 40 frames (~1 minute de course)
- ✅ Système de boost avec cooldown de 3 secondes
- ✅ Ligne d'arrivée visible et arrêt de l'écran à la fin
- ✅ Sauvegarde automatique de tous les scores (pas seulement les records)
- ✅ Interface `IGamePage` pour unifier les pages de jeu
- ✅ Menu pause universel fonctionnant avec les deux modes
- ✅ Nettoyage complet du code (suppression de tous les logs de debug)
- ✅ Configuration des touches via `settings.ini`
- ✅ Touche pause par défaut changée de PAUSE à P

**Bon jeu ! 🏁**
