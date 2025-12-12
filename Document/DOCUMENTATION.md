# 📚 Documentation - Crazy Roads

Ce dossier contient toute la documentation du projet Crazy Roads.

## 📂 Structure

### 📖 README.md
Documentation principale du projet contenant :
- Description du jeu
- Instructions d'installation et de lancement
- Contrôles et fonctionnalités
- Structure du projet
- Guide de configuration

### 📊 Coverage (Couverture de tests)
Le dossier `coverage/` contient le rapport de couverture de code généré par JaCoCo.

**Générer le rapport :**
```bash
cd Crazy_Roads
mvnw.cmd test jacoco:report
```

**Consulter :**
Ouvrir `coverage/index.html` dans votre navigateur
```powershell
Start-Process "..\Document\coverage\index.html"
```

**Résultats actuels :**
- ✅ 120 tests unitaires
- ✅ 27 classes analysées
- ✅ 17% de couverture d'instruction
- ✅ 0 erreurs

### 📚 Javadoc (Documentation technique)
Le dossier `javadoc/` contient la documentation technique de toutes les classes Java.

**Générer la Javadoc :**
```bash
cd Crazy_Roads
mvnw.cmd javadoc:javadoc
```

**Consulter :**
Ouvrir `javadoc/index.html` dans votre navigateur
```powershell
Start-Process "..\Document\javadoc\index.html"
```

## 🔄 Mise à jour de la documentation

### Après modification du code
```bash
# 1. Tester le code
mvnw.cmd test

# 2. Générer le rapport de couverture
mvnw.cmd jacoco:report

# 3. Générer la Javadoc
mvnw.cmd javadoc:javadoc
```

### Bonnes pratiques
- ✅ Documenter toutes les classes publiques avec Javadoc
- ✅ Ajouter des tests pour chaque nouvelle fonctionnalité
- ✅ Maintenir le README.md à jour
- ✅ Régénérer la documentation après chaque changement majeur

## 📊 Statistiques du projet

### Tests
- **Total de tests :** 120
- **Taux de réussite :** 100%
- **Packages testés :** 6

### Classes principales
- **generation/** : Génération procédurale d'obstacles
- **gui/** : Interface utilisateur et HUD
- **managers/** : Gestion de la musique, des ressources et des scores
- **models/** : Modèles de données (Voiture, Carte, Frame, Obstacle)
- **modes/** : Modes de jeu (Mario Kart, Subway Surfers)

## 🎮 Modes de jeu

### Mode Mario Kart
- Course avec ligne d'arrivée (60 frames)
- Système de boost (touche ESPACE)
- Score basé sur le temps de course

### Mode Subway Surfers
- Course infinie
- Score basé sur la distance parcourue
- Génération procédurale d'obstacles

---

**Dernière mise à jour :** 23 novembre 2025
