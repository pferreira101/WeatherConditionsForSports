/*!

=========================================================
* Black Dashboard React v1.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/black-dashboard-react
* Copyright 2020 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/black-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Climate from "views/Climate.js";
import Weather from "views/Weather.js"
import BestPractice from "views/BestPractice.js";
import Map from "views/Map.js";
import TableList from "views/TableList.js";

var routes = [
  {
    path: "/weather",
    name: "Weather",
    icon: "tim-icons icon-world",
    component: Weather,
    layout: "/admin",
  },
  {
    path: "/climate",
    name: "Climate",
    icon: "tim-icons icon-world",
    component: Climate,
    layout: "/admin",
  },
  {
    path: "/bestpractice",
    name: "Best Practice Time",
    icon: "tim-icons icon-user-run",
    component: BestPractice,
    layout: "/admin",
  },
  {
    path: "/map",
    name: "Map",
    icon: "tim-icons icon-map-big",
    component: Map,
    layout: "/admin",
  },
  {
    path: "/tables",
    name: "Table List",
    icon: "tim-icons icon-puzzle-10",
    component: TableList,
    layout: "/admin",
  },
];
export default routes;
