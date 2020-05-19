import firebase from "firebase";

var firebaseConfig = {
  apiKey: "AIzaSyCh3PpqAMOZ2R10kCn33xa8oa95XjGFiWg",
  authDomain: "sa-tp2-1920.firebaseapp.com",
  databaseURL: "https://sa-tp2-1920.firebaseio.com",
  projectId: "sa-tp2-1920",
  storageBucket: "sa-tp2-1920.appspot.com",
  messagingSenderId: "1010836247962",
  appId: "1:1010836247962:web:bd2e1ec3fbc2f678081623",
  //measurementId: "G-L843CT8G4T",
};

firebase.initializeApp(firebaseConfig);

export default firebase;
