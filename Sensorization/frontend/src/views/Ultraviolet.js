import React from "react";

// core components
import { Row, Col } from "reactstrap";
import PropagateLoader from "react-spinners/PropagateLoader";
import LineChart from "components/Ultraviolet/LineChart.js";
import UVTable from "components/Ultraviolet/UVTable.js";

// firestore
import firebase from "firebase/firestore.js";

let oneInfoPerDay = (docsId, docsData) => {
  let infoWeekDay = [];
  let infoWeekUVMax = [];

  let i;
  for (i = 0; i < docsId.length; i++) {
    let date = new Date(docsId[i]);

    let day = date.getDate().toString();
    let month = date.getMonth() + 1;
    month = month.toString();
    let dayofweek = date.getDay().toString();

    switch (dayofweek) {
      case "0":
        dayofweek = "SUN";
        break;
      case "1":
        dayofweek = "MON";
        break;
      case "2":
        dayofweek = "TUE";
        break;
      case "3":
        dayofweek = "WED";
        break;
      case "4":
        dayofweek = "THU";
        break;
      case "5":
        dayofweek = "FRI";
        break;
      case "6":
        dayofweek = "SAT";
        break;
    }

    let key = dayofweek.concat("-", day, "/", month);

    if (!infoWeekDay.includes(key)) {
      infoWeekDay.push(key);
      infoWeekUVMax.push(docsData[i]);
    }
  }
  return {
    array1: infoWeekDay,
    array2: infoWeekUVMax,
  };
};

let onlySunValues = (ids, st1, st2, st3, st4, st5, st6) => {
  let sunvaluesIds = [];
  let sunvalues1 = [];
  let sunvalues2 = [];
  let sunvalues3 = [];
  let sunvalues4 = [];
  let sunvalues5 = [];
  let sunvalues6 = [];

  let i;
  for (i = 0; i < st1.length; i++) {
    if (st1[i] == "None" && st1[i + 1] != "None") {
      sunvaluesIds.push("-");
      sunvalues1.push("-");
      sunvalues2.push("-");
      sunvalues3.push("-");
      sunvalues4.push("-");
      sunvalues5.push("-");
      sunvalues6.push("-");
    }
    if (st1[i] != "None") {
      sunvaluesIds.push(ids[i]);
      sunvalues1.push(st1[i]);
      sunvalues2.push(st2[i]);
      sunvalues3.push(st3[i]);
      sunvalues4.push(st4[i]);
      sunvalues5.push(st5[i]);
      sunvalues6.push(st6[i]);
    }
  }

  return {
    array0: sunvaluesIds,
    array1: sunvalues1,
    array2: sunvalues2,
    array3: sunvalues3,
    array4: sunvalues4,
    array5: sunvalues5,
    array6: sunvalues6,
  };
};

class Ultraviolet extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      idsUv: [],
      uvData: [],
      idsUvMax: [],
      uvMaxData: [],
      idsSt: [],
      st1: [],
      st2: [],
      st3: [],
      st4: [],
      st5: [],
      st6: [],
    };

    this.getUVData();
  }

  getUVData() {
    const db = firebase.firestore();

    db.collection("UV")
      .get()
      .then((querySnapshot) => {
        let docs = querySnapshot.docs.slice(-24);
        let docs_data = docs.map((doc) => doc.data());

        let ids = docs.map((doc) => doc.id);

        let docsWeek = querySnapshot.docs.slice(-168); // 24 x 7
        let docsWeek_ids = docsWeek.map((doc) => doc.id);
        let docsWeek_data = docsWeek.map((doc) => doc.data());
        docsWeek_data = docsWeek_data.map((data) => data["uv_max"]);

        let result1 = oneInfoPerDay(docsWeek_ids, docsWeek_data);

        let st1 = docs_data.map((data) => data["st1"]);
        let st2 = docs_data.map((data) => data["st2"]);
        let st3 = docs_data.map((data) => data["st3"]);
        let st4 = docs_data.map((data) => data["st4"]);
        let st5 = docs_data.map((data) => data["st5"]);
        let st6 = docs_data.map((data) => data["st6"]);

        let result2 = onlySunValues(ids, st1, st2, st3, st4, st5, st6);

        this.setState({
          idsUv: ids,
          uvData: docs_data.map((data) => data["uv"]),
          idsUvMax: result1.array1,
          uvMaxData: result1.array2,
          idsSt: result2.array0,
          st1: result2.array1,
          st2: result2.array2,
          st3: result2.array3,
          st4: result2.array4,
          st5: result2.array5,
          st6: result2.array6,
        });
      });
  }

  render() {
    if (
      this.state.idsUv.length === 0 ||
      this.state.uvData.length === 0 ||
      this.state.idsUvMax.length === 0 ||
      this.state.uvMaxData.length === 0
    ) {
      return (
        <div className="content">
          <Row>
            <Col
              xs="12"
              className="text-center"
              style={{ paddingTop: "30%", paddingLeft: "50%" }}
            >
              <PropagateLoader color={"#1E8AF7"} />
            </Col>
          </Row>
        </div>
      );
    }
    return (
      <div className="content">
        <Row>
          <Col xs="12">
            <LineChart
              title="UV Index"
              subtitle="Braga, Portugal"
              idsUv={this.state.idsUv}
              uvData={this.state.uvData}
              idsUvMax={this.state.idsUvMax}
              uvMaxData={this.state.uvMaxData}
            />
          </Col>
        </Row>
        <Row>
          <Col lg="8">
            <UVTable
              labels={this.state.idsSt}
              st1={this.state.st1}
              st2={this.state.st2}
              st3={this.state.st3}
              st4={this.state.st4}
              st5={this.state.st5}
              st6={this.state.st6}
            />
          </Col>
          <Col lg="4"></Col>
        </Row>
      </div>
    );
  }
}

export default Ultraviolet;
