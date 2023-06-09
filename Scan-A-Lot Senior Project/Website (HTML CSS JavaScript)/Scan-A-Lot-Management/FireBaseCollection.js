//This will be the collection for Officers. Will make a new JS file unique for each other table. 
import {initializeApp} from "https://www.gstatic.com/firebasejs/9.4.0/firebase-app.js";
import { getFirestore, collection, getDocs, doc} from 'https://www.gstatic.com/firebasejs/9.4.0/firebase-firestore.js';

const firebaseConfig = {
  apiKey: "AIzaSyDIEtCfoSgt-Ka56fFwouFDvEXId0Xrk78",
  authDomain: "scan-a-lot.firebaseapp.com",
  projectId: "scan-a-lot",
  storageBucket: "scan-a-lot.appspot.com",
  messagingSenderId: "816922417821",
  appId: "1:816922417821:web:ba0a3811f84a8cbbacd274",
  measurementId: "G-LVFHL8LFNV"
};

//If firebase connection is not initialized initialize it
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);
 
//List of firestore collections
var arrTickets = [];
var arrVehicles = [];
var arrOfficers = [];
var arrOffenses = [];
var arrLots = []

//Get Firestore Officers Collection
const querySnapshotTickets = await getDocs(collection(db, "Tickets"))
querySnapshotTickets.forEach((doc) => {
  arrTickets.push(doc.data());
});
//Get Firestore Officers Collection
const querySnapshotVehicles = await getDocs(collection(db, "Vehicles"))
querySnapshotVehicles.forEach((doc) => {
  arrVehicles.push(doc.data());
});
//Get Firestore Officers Collection
const querySnapshotOfficers = await getDocs(collection(db, "Officers"))
querySnapshotOfficers.forEach((doc) => {
  arrOfficers.push(doc.data());
});

//Get Firestore Offenses Collection
const querySnapshotLots = await getDocs(collection(db, "ParkingLots"))
querySnapshotLots.forEach((doc) => {
  arrLots.push(doc.data());
});

//Get Firestore Offenses Collection
const querySnapshotOffenses = await getDocs(collection(db, "Offenses"))
querySnapshotOffenses.forEach((doc) => {
  arrOffenses.push(doc.data());
});
        

      
//Creates all the tables in the database
function getAllDataOnce(){
  //Tickets
  AddAllItemsToTheTicketsTable(arrTickets);
  //Vehicles
  AddAllItemsToTheVehiclesTable(arrVehicles);
  //Officers 
  AddAllItemsToTheOfficerTable(arrOfficers);
  //ParkingLots
  AddAllItemsToTheParkingLotsTable(arrLots);
  //Offenses
  AddAllItemsToTheOffensesTable(arrOffenses);
}

//filling the table
var offNo = 0;
var tTicketsbody = document.getElementById("tbody1");
var tVehiclesbody = document.getElementById("tbody2");
var tOfficerbody = document.getElementById("tbody3");
var tParkingLotsbody = document.getElementById("tbody4");
var tOffensesbody = document.getElementById("tbody5");

function AddItemToOfficerTable(LastName, FirstName, Username, Email){
  var trow = document.createElement('tr');
  var td1 = document.createElement('td');
  var td2 = document.createElement('td');
  var td3 = document.createElement('td');
  var td4 = document.createElement('td');
  var td5 = document.createElement('dropbtn')

  //td1.innerHTML = ++offNo;
  
  td1.innerHTML = LastName;
  td2.innerHTML = FirstName;
  td3.innerHTML = Username;
  td4.innerHTML = Email;
  td5.innerHTML = 'Modify';

  trow.appendChild(td1);
  trow.appendChild(td2);
  trow.appendChild(td3);
  trow.appendChild(td4);
  trow.appendChild(td5);

  tOfficerbody.appendChild(trow);
}

function AddAllItemsToTheOfficerTable(OfficerDocList){
  offNo=0;
  tOfficerbody.innerHTML="";
  OfficerDocList.forEach(element => {
    AddItemToOfficerTable(element.LastName, element.FirstName, element.Username, element.Email);
    
  });
}

function AddItemToOffensesTable(OffenseType, Fine){
  var trow = document.createElement('tr');
  var td1 = document.createElement('td');
  var td2 = document.createElement('td');
  var td3 = document.createElement('dropbtn')


  //td1.innerHTML = ++offNo;
  
  td1.innerHTML = OffenseType;
  td2.innerHTML = Fine;
  td3.innerHTML = 'Modify';

  trow.appendChild(td1);
  trow.appendChild(td2);
  trow.appendChild(td3);

  tOffensesbody.appendChild(trow);
  
}
  
function AddAllItemsToTheOffensesTable(OffenseDocList){
  offNo=0;
  tOffensesbody.innerHTML="";
  OffenseDocList.forEach(element => {
    AddItemToOffensesTable(element.OffenseType, element.FineAmount);
  });

}  

function AddItemToParkingLotsTable(LotName, MaxLatitude, MaxLongitude, MinLatitude, MinLongitude){
  var trow = document.createElement('tr');
  var td1 = document.createElement('td');
  var td2 = document.createElement('td');
  var td3 = document.createElement('td');
  var td4 = document.createElement('td');
  var td5 = document.createElement('td');
  var td6 = document.createElement('dropbtn')


  //td1.innerHTML = ++offNo;
  
  td1.innerHTML = LotName;
  td2.innerHTML = MaxLatitude;
  td3.innerHTML = MaxLongitude;
  td4.innerHTML = MinLatitude;
  td5.innerHTML = MinLongitude;
  td6.innerHTML = 'Modify';

  trow.appendChild(td1);
  trow.appendChild(td2);
  trow.appendChild(td3);
  trow.appendChild(td4);
  trow.appendChild(td5);
  trow.appendChild(td6);

  tParkingLotsbody.appendChild(trow);
  
}
  
function AddAllItemsToTheParkingLotsTable(ParkingLotsDocList){
  offNo=0;
  tParkingLotsbody.innerHTML="";
  ParkingLotsDocList.forEach(element => {
    AddItemToParkingLotsTable(element.LotName, element.MaxLatitude, element.MaxLongitude, element.MinLatitude, element.MinLongitude);
    
  });
}

function AddItemToVehiclesTable(Color, IDNum, LicenseNum, LicenseState, Make, Model, OwnerFirstName, OwnerLastName, ParkingLot){
  var trow = document.createElement('tr');
  var td1 = document.createElement('td');
  var td2 = document.createElement('td');
  var td3 = document.createElement('td');
  var td4 = document.createElement('td');
  var td5 = document.createElement('td');
  var td6 = document.createElement('td');
  var td7 = document.createElement('td');
  var td8 = document.createElement('td');
  var td9 = document.createElement('td');
  var td10 = document.createElement('dropbtn')
  
  td1.innerHTML = Color;
  td2.innerHTML = IDNum;
  td3.innerHTML = LicenseNum;
  td4.innerHTML = LicenseState;
  td5.innerHTML = Make;
  td6.innerHTML = Model;
  td7.innerHTML = OwnerFirstName;
  td8.innerHTML = OwnerLastName;
  td9.innerHTML = ParkingLot;
  td10.innerHTML = 'Modify';

  trow.appendChild(td1);
  trow.appendChild(td2);
  trow.appendChild(td3);
  trow.appendChild(td4);
  trow.appendChild(td5);
  trow.appendChild(td6);
  trow.appendChild(td7);
  trow.appendChild(td8);
  trow.appendChild(td9);
  trow.appendChild(td10);

  tVehiclesbody.appendChild(trow);
  
}
  
function AddAllItemsToTheVehiclesTable(VehiclesDocList){
  offNo=0;
  tVehiclesbody.innerHTML="";
  VehiclesDocList.forEach(element => {
    AddItemToVehiclesTable(element.Color, element.IDNum, element.LicenseNum, element.LicenseState, element.Make, element.Model, element.OwnerFirstName, element.OwnerLastName, element.ParkingLot);    
  });
}
  
function AddItemToTicketsTable(CarMake, CarModel, FineAmount, LicenseNum, Offense, Officer, ParkingLot, TicketNum, Time){
  var trow = document.createElement('tr');
  var td1 = document.createElement('td');
  var td2 = document.createElement('td');
  var td3 = document.createElement('td');
  var td4 = document.createElement('td');
  var td5 = document.createElement('td');
  var td6 = document.createElement('td');
  var td7 = document.createElement('td');
  var td8 = document.createElement('td');
  var td9 = document.createElement('td');
  var td10 = document.createElement('dropbtn')

  td1.innerHTML = CarMake;
  td2.innerHTML = CarModel; 
  td3.innerHTML = FineAmount;
  td4.innerHTML = LicenseNum;
  td5.innerHTML = Offense;
  td6.innerHTML = Officer;
  td7.innerHTML = ParkingLot;
  td8.innerHTML = TicketNum;
  td9.innerHTML = Time;
  td10.innerHTML = 'Modify';
  
  trow.appendChild(td1);
  trow.appendChild(td2);
  trow.appendChild(td3);
  trow.appendChild(td4);
  trow.appendChild(td5);
  trow.appendChild(td6);
  trow.appendChild(td7);
  trow.appendChild(td8);
  trow.appendChild(td9);
  trow.appendChild(td10);
  

  tTicketsbody.appendChild(trow);
  
}

function AddAllItemsToTheTicketsTable(TicketsDocList){
  offNo=0;
  tTicketsbody.innerHTML="";
  TicketsDocList.forEach(element => {
    AddItemToTicketsTable(element.CarMake, element.CarModel, element.FineAmount, element.LicenseNum, element.Offense, element.Officer, element.ParkingLot, element.TicketNum, element.Time);
    
  });

}

//Once window loads it gets data from firestore
window.onload = getAllDataOnce();

//Export arrays so they can be added to 
export {arrVehicles};
export {arrOfficers};
export {arrLots};
export {arrOffenses};

//Modify dropdown
class ModifyBtn {
  constructor(){
    const button = document.createElement('button')
    button.innerText = 'Modify'
    button.id = 'dropbtn'
    button.addEventListener('click', displayDrop,() => {
    })

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}
//edit tabel rows
function productAddToTable() {
  // First check if a <tbody> tag exists, add one if not
  if ($("#productTable tbody").length == 0) {
      $("#productTable").append("<tbody></tbody>");
  }

  // Append product to the table
  $("#productTable tbody").append(
      "<tr>" +
      "<td>" + $("#productname").val() + "</td>" +
      "<td>" + $("#introdate").val() + "</td>" +
      "<td>" + $("#url").val() + "</td>" +
      "<td>" +
      "<button type='button' 
               onclick='productDelete(this);' 
               class='btn btn-default'>" +;
      `<span class='glyphicon glyphicon-remove' /></button></td></tr>`);
}
//remove tabel row 
function productDelete(ctl) {
  $(ctl).parents("tr").remove();
}
  }
}

//tab stays selected
function OpenTab(evt, cityName) {
  var i, x, tablinks;
  x = document.getElementsByClassName("id");
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";
  }
  tablinks = document.getElementsByClassName("id");
  for (i = 0; i < x.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" #8A2BE2 ", "");
  }
  document.getElementById(id).style.display = "block";
  evt.currentTarget.className += " #8A2BE2";
}
