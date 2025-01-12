# CodeBreaker UTBM

## Description
CodeBreaker UTBM est un jeu inspiré de la machine Enigma, adapté au contexte de l'UTBM. Le joueur doit déchiffrer une combinaison de notes (A, B, C, D, F) en utilisant sa logique et les indices fournis. Le jeu propose trois niveaux de difficulté représentant les différentes étapes du cursus UTBM :

- **TC01 - Facile** : Pour les débutants en Tronc Commun
- **TC02 - Moyen** : Pour les étudiants avancés en TC
- **Branche - Difficile** : Pour les étudiants en branche, avec des règles plus complexes

## Règles du Jeu
- Le joueur doit deviner une combinaison de 3 notes
- Chaque note peut être : A (18), B (15), C (12), D (9), F (5)
- À chaque tentative, le jeu fournit des indices sur :
  - Les notes correctement placées
  - Les notes présentes mais mal placées
- Des règles supplémentaires s'ajoutent selon le niveau de difficulté :
  - Facile : Règles de base
  - Moyen : Ajout de contraintes sur les notes minimales
  - Difficile : Notes consécutives et autres règles avancées

## Fonctionnalités
- Interface graphique intuitive
- Système de score basé sur :
  - Le nombre de tentatives utilisées
  - Le temps de résolution
  - La difficulté choisie
- Tableau des meilleurs scores
- Feedback en temps réel
- Maximum de 12 tentatives par partie

## Technologies Utilisées
- Java 21
- Interface graphique : Java Swing
- Gestion de projet : Maven

## Installation
1. Cloner le dépôt
2. Lancer simplement le fichier Main.java avec Intellij IDEA

## Développé dans le cadre du cours AP4B
Ce projet a été réalisé dans le cadre du cours AP4B à l'UTBM.

## Auteurs
- Girard Mathéo 
- Simon Nicod
- Simon Nguyen