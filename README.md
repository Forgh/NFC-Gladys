# NFC-Gladys

Application utilisée conjointement avec le projet domotique Gladys ( http://gladysproject.com ) permettant d’interagir avec des objets connectés.
Le but cette application est de permettre aux utilisateurs de créer des « ambiances », des scénarios consistant en une succession d’actions à réaliser avec les objets (allumer/éteindre les lumières, démarrer une playlist de musique, connaitre la météo, etc)
qu’ils pourront alors inscrire sur des tags NFC. Par la suite, les utilisateurs n’auront plus qu’à rapprocher leur téléphone du tag NFC pour exécuter l’ambiance choisie.

L’objectif sera donc de créer un écrivain/interpréteur de tag NFC pour :
* allumer/éteindre des lampes Phillips Hue, 
* démarrer/arrêter une playlist de musique, 
* stopper le réveil.
Et d’envoyer les requêtes http nécessaires.

En fonction des objets, nous ajouterons à l’application la possibilité de visualiser l’état des appareils.


## Installation

### Android
Compilez et executez le code source de dépot (sauf le contenu du dossier gladys_scripts).

Lors de la première utilisation, il vous sera demandé l'adresse IP du raspberry Pi où se trouve Gladys ainsi que vos identifiants de connexion.
Si vous n'avez pas créé de compte sur votre interface Gladys, vous pouvez y accéder à l'adresse :
http://adresse.ip.raspberry.pi:1337/


### Gladys
Vous devez impérativement posséder une installation récente du programme Gladys (voir site du projet ci-dessus).

Pour l'utilisation des lampes connectées Philips Hue, vous devez impérativement créer un utilisateur.

Remplacez ensuite les fichiers suivant par les fichiers du même nom dans le dossier scripts_gladys de ce dépot:
 
* /usr/local/lib/node_modules/gladys/api/script/alarm.js
* /usr/local/lib/node_modules/gladys/api/config/policies.js
* /usr/local/lib/node_modules/gladys/api/controllers/MusicControllers.js
* /usr/local/lib/node_modules/gladys/api/services/MusicService.js

### Philips Hue
Utilisez pour cela le script RegisterUser présent dans le dossier scripts_gladys.

Remplacez l'adresse IP par celle de votre pont puis faites la commande :
node RegisterUser > file.txt


Puis rajoutez les fichiers suivant :

* /usr/local/lib/node_modules/gladys/api/controllers/HueController.js
* /usr/local/lib/node_modules/gladys/api/models/Hue.js
* /usr/local/lib/node_modules/gladys/api/services/HueService.js
