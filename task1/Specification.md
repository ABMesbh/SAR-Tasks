

### Spécification des canaux de communication

#### 1. Vue d'ensemble

Ce système permet à des tâches de communiquer entre elles via un **Broker** qui gère les connexions. Les tâches peuvent établir des canaux de communication pour échanger des données, que ce soit dans le même processus ou sur des machines différentes. Chaque tâche se connecte à un **Broker**, qui assure que les communications sont bien gérées entre les tâches en fonction de leurs noms et ports.

#### **2. Classes principales**

##### **2.1. Broker**
Le **Broker** agit comme une fabrique de canaux de communication et gère les connexions entre les tâches. Les tâches doivent partager le même broker pour pouvoir communiquer.

- **Propriétés :**
  - Chaque **Broker** a un **nom unique**. La combinaison du **nom** et du **port** garantit que le broker est identifiable de manière unique.

- **Fonctions principales :**
  - `Channel accept(int port)`: Accepte une connexion entrante sur un port spécifique et retourne un **Channel** pour établir la communication.
  - `Channel connect(String name, int port)`: Se connecte à une autre tâche via le **nom** et le **port**, et crée un **Channel** pour échanger des données.

##### **2.2. Channel**
Le **Channel** représente le canal de communication utilisé pour échanger des données entre les tâches. Il garantit le transfert ordonné des données (FIFO).

- **Propriétés :**
  - Le **Channel** est un flux FIFO sans perte, garantissant que les données sont reçues dans le même ordre que leur envoi.

- **Méthodes principales :**
  - `int read(byte[] buffer, int offset, int length)`: Lit des données du **Channel** et retourne le nombre d'octets lus.
  - `int write(byte[] buffer, int offset, int length)`: Écrit des données dans le **Channel** et retourne le nombre d'octets écrits.
  - `void disconnect()`: Ferme la connexion du **Channel**.
  - `boolean disconnected()`: Indique si le **Channel** est déconnecté.

##### **2.3. Task**
La classe **Task** représente une unité d'exécution (thread) qui communique avec d'autres tâches à travers des **Channels** gérés par un **Broker**.

- **Propriétés :**
  - Une **Task** est associée à un **Broker** qui gère ses canaux de communication avec d'autres tâches.

- **Méthodes principales :**
  - `Task(Broker broker, Runnable action)`: Crée une tâche associée à un **Broker** et définit une action à exécuter.
  - `static Broker getBroker()`: Retourne le **Broker** utilisé par la tâche courante pour gérer ses connexions.

#### **3. Règles**  

 - Le Broker est identifié par une combinaison unique de nom et de port, et il gère les connexions entre différentes tâches.
 - Plusieurs tâches peuvent utiliser un port commun
 - Les données échangées via un Channel suivent un ordre strict et aucune donnée ne doit être perdue.
 - Les canaux de communication doivent être thread-safe, permettant à plusieurs threads de lire ou d'écrire en même temps sans risque de corruption des données.
  