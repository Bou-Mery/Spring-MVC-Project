
---

# 📘 Application de Surveillance des Examens**

## 📋 **Description**
Ce projet est une application de surveillance des examens développée avec **Spring MVC** et **Spring Boot**.  
Elle permet de gérer les **sessions d'examens** (normales ou de rattrapage) et de fournir un tableau de bord centralisé pour administrer différents éléments tels que :  
- **Départements**  
- **Enseignants**  
- **Modules**  
- **Locaux**  
- **Examens**  
- **Surveillance des professeurs**  

L'application intègre un **système d'authentification** pour accéder à une interface utilisateur intuitive, développée avec **Thymeleaf**. La base de données utilisée est **MySQL**.

---

## 🗂️ **Table des Matières**
1. [Technologies Utilisées](#-technologies-utilisées)  
2. [Fonctionnalités Principales](#-fonctionnalités-principales)  
3. [Architecture Spring MVC](#-architecture-spring-mvc)  
4. [Structure du Projet](#-structure-du-projet)  
5. [Installation et Configuration](#-installation-et-configuration)  
6. [Aperçu des Interfaces](#-aperçu-des-interfaces)  
7. [Contributions](#-contributions)  

---

## 🔧 **Technologies Utilisées**
- **Backend** : Spring Boot (Spring MVC, Spring Security, Spring Data JPA)  
- **Frontend** : Thymeleaf, Bootstrap  
- **Base de données** : MySQL  
- **Gestionnaire de dépendances** : Maven  
- **Outils de développement** : IntelliJ IDEA, Postman  

---

## 🚀 **Fonctionnalités Principales**
- **Authentification** : Connexion sécurisée pour les administrateurs.  
- **Gestion des Sessions** : Création et gestion des sessions (normal/rattrapage).  
- **Tableau de Bord** : Interface centralisée avec menu latéral pour gérer :  
  - Départements  
  - Enseignants  
  - Modules  
  - Locaux  
  - Examens  
  - Assignation des professeurs aux examens  
- **Exportation** : Impression des plannings d'examens et de surveillance.  

---

## 📐 **Architecture Spring MVC**
L'application est basée sur l'architecture **Spring MVC**, qui sépare les responsabilités en trois couches :  
1. **Model** : Représente les données manipulées par l'application.  
   - Exemple : `Session`, `Enseignant`, `Module`.  
2. **View** : Représente l'interface utilisateur.  
   - Outil utilisé : **Thymeleaf** pour le rendu des pages HTML dynamiques.  
3. **Controller** : Gère les requêtes HTTP et les interactions utilisateur.  
   - Exemple : `SessionController`, `EnseignantController`.  

**Fonctionnement Du Spring MVC** :  
![Capture d'écran 2024-12-15 185814](https://github.com/user-attachments/assets/ee7f3140-e7cd-4798-9d08-6c291b5d38a6)


## 📂 **Structure du Projet**
![image](https://github.com/user-attachments/assets/11dddc34-9293-477e-9d59-eb4b58958021)


---

## 🛠️ **Installation et Configuration**

### 1. **Prérequis**
- **Java 17+**  
- **MySQL** (ou tout autre SGBD)  
- **Maven**  

### 2. **Cloner le Projet**
```bash
git clone https://github.com/votre-repo/exam-surveillance.git
cd exam-surveillance
```

### 3. **Configurer la Base de Données**
Créez une base de données MySQL :  
```sql
CREATE DATABASE exam_surveillance;
```

Configurez les paramètres dans `application.properties` :  
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/exam_surveillance
spring.datasource.username=VOTRE_USERNAME
spring.datasource.password=VOTRE_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### 4. **Lancer l’Application**
Compilez et exécutez l’application :  
```bash
mvn spring-boot:run
```

Accédez à l'application : [http://localhost:8080](http://localhost:8080)

---

## 🖼️ **Aperçu des Interfaces**
1. **Page de Connexion**  
![Login](https://via.placeholder.com/800x400)  

2. **Tableau de Bord**  
![Dashboard](https://via.placeholder.com/800x400)  

3. **Gestion des Sessions**  
![Gestion des Sessions](https://via.placeholder.com/800x400)  

---

## 🙌  **Contributeurs**
- BOUKHRAIS Meryem
- SAKHR Niama
- CHAJARI Salma
- EL GHARBI Abdallah

---

## 📄 **Licence**
Ce projet est sous licence [MIT].

--- 
