# NFC-Gladys

Application utilis�e conjointement avec le projet domotique Gladys ( http://gladysproject.com ) permettant d�interagir avec des objets connect�s.
Le but cette application est de permettre aux utilisateurs de cr�er des � ambiances �, des sc�narios consistant en une succession d�actions � r�aliser avec les objets (allumer/�teindre les lumi�res, d�marrer une playlist de musique, connaitre la m�t�o, etc)
qu�ils pourront alors inscrire sur des tags NFC. Par la suite, les utilisateurs n�auront plus qu�� rapprocher leur t�l�phone du tag NFC pour ex�cuter l�ambiance choisie.

L�objectif sera donc de cr�er un �crivain/interpr�teur de tag NFC pour :
* allumer/�teindre des lampes Phillips Hue, 
* d�marrer/arr�ter une playlist de musique, 
* stopper le r�veil.
Et d�envoyer les requ�tes http n�cessaires.

En fonction des objets, nous ajouterons � l�application la possibilit� de visualiser l��tat des appareils.


## Installation

### Android
Compilez et executez le code source de d�pot (sauf le contenu du dossier gladys_scripts).

Lors de la premi�re utilisation, il vous sera demand� l'adresse IP du raspberry Pi o� se trouve Gladys ainsi que vos identifiants de connexion.
Si vous n'avez pas cr�� de compte sur votre interface Gladys, vous pouvez y acc�der � l'adresse :
http://adresse.ip.raspberry.pi:1337/


### Gladys
Vous devez imp�rativement poss�der une installation r�cente du programme Gladys (voir site du projet ci-dessus).

Pour l'utilisation des lampes connect�es Philips Hue, vous devez imp�rativement cr�er un utilisateur.

Remplacez ensuite les fichiers suivant par les fichiers du m�me nom dans le dossier scripts_gladys de ce d�pot:
 
* /usr/local/lib/node_modules/gladys/api/script/alarm.js
* /usr/local/lib/node_modules/gladys/api/config/policies.js
* /usr/local/lib/node_modules/gladys/api/controllers/MusicControllers.js
* /usr/local/lib/node_modules/gladys/api/services/MusicService.js

### Philips Hue
Utilisez pour cela le script RegisterUser pr�sent dans le dossier scripts_gladys.

Remplacez l'adresse IP par celle de votre pont puis faites la commande :
node RegisterUser > file.txt


Puis rajoutez les fichiers suivant :

* /usr/local/lib/node_modules/gladys/api/controllers/HueController.js
* /usr/local/lib/node_modules/gladys/api/models/Hue.js
* /usr/local/lib/node_modules/gladys/api/services/HueService.js
