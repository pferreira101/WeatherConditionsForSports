import React from "react";

// core components
import { Row, Col } from "reactstrap";
import PropagateLoader from "react-spinners/PropagateLoader";
import LineChart from "components/Ultraviolet/LineChart.js";

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

class Ultraviolet extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      idsUv: [],
      uvData: [],
      idsUvMax: [],
      uvMaxData: [],
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

        let docsWeek = querySnapshot.docs.slice(-168); // 24 x 7
        let docsWeek_ids = docsWeek.map((doc) => doc.id);
        let docsWeek_data = docsWeek.map((doc) => doc.data());
        docsWeek_data = docsWeek_data.map((data) => data["uv_max"]);

        let result = oneInfoPerDay(docsWeek_ids, docsWeek_data);

        this.setState({
          idsUv: docs.map((doc) => doc.id),
          uvData: docs_data.map((data) => data["uv"]),
          idsUvMax: result.array1,
          uvMaxData: result.array2,
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
        <LineChart
          title="Ultraviolet Radiation"
          subtitle="Braga, Portugal"
          idsUv={this.state.idsUv}
          uvData={this.state.uvData}
          idsUvMax={this.state.idsUvMax}
          uvMaxData={this.state.uvMaxData}
        />
      </div>
    );
  }
}

export default Ultraviolet;
