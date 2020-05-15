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
    let date = (new Date(docsId[i])).toDateString();

    if (!infoWeekDay.includes(date)) {
      infoWeekDay.push(date);
      infoWeekUVMax.push(docsData[i]);
    }
    else if(docsData[i] > infoWeekUVMax[infoWeekUVMax.length-1]){ // alguns dias tem variacoes no max uv
      infoWeekUVMax[infoWeekUVMax.length-1] = docsData[i];
    }
  }
  return {
    array1: infoWeekDay.slice(-8, -1),
    array2: infoWeekUVMax.slice(-8, -1),
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
      specificDay: this.props.date,
    };

    this.getUVData();
  }

  getUVData() {
    const db = firebase.firestore();

    db.collection("UV")
      .get()
      .then((querySnapshot) => {
        let docs = querySnapshot.docs;
        let docs_data = docs.map((doc) => doc.data());

        let ids = docs.map((doc) => doc.id);

        let docsWeek = querySnapshot.docs.slice(-192); // 24 x 8 (mostrar os ultimos 7 dias e excluir o dia atual)
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


        this.setState({
          idsUv: ids,
          uvData: docs_data.map((data) => data["uv"]),
          idsUvMax: result1.array1,
          uvMaxData: result1.array2,
          idsSt: ids,
          st1: st1,
          st2: st2,
          st3: st3,
          st4: st4,
          st5: st5,
          st6: st6,
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
              title= {this.state.specificDay ? "UV Index - " + this.state.specificDay :"UV Index"}
              subtitle="Braga, Portugal"
              idsUv={this.state.idsUv}
              uvData={this.state.uvData}
              idsUvMax={this.state.idsUvMax}
              uvMaxData={this.state.uvMaxData}
              specificDay={this.state.specificDay}
            />
          </Col>
        </Row>
        <Row>
          <Col lg="9">
            <UVTable
              labels={this.state.idsSt}
              st1={this.state.st1}
              st2={this.state.st2}
              st3={this.state.st3}
              st4={this.state.st4}
              st5={this.state.st5}
              st6={this.state.st6}
              specificDay={this.state.specificDay}
            />
          </Col>
          <Col lg="3">
            <Row>
              <img
                height="95%"
                width="95%"
                src={require("assets/img/tfs1.png")}
              />
            </Row>
            <Row>
              <img
                height="95%"
                width="95%"
                src={require("assets/img/tfs2.png")}
              />
            </Row>
          </Col>
        </Row>
      </div>
    );
  }
}

export default Ultraviolet;
