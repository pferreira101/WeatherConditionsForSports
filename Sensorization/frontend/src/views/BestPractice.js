import React from "react";

// core components
import { Row, Col } from "reactstrap";
import PropagateLoader from "react-spinners/PropagateLoader";
import LineChart from "components/BestPractice/LineChart.js";

// firestore
import firebase from "firebase/firestore.js";

class BestPractice extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      hours: [],
      humidity: [],
      uv: [],
      weather: [],
    };

    this.getPredictionsData();
  }

  getPredictionsData() {
    const db = firebase.firestore();

    db.collection("Predictions")
      .get()
      .then((querySnapshot) => {
        let docs_hours = querySnapshot.docs.map((doc) => doc.data()["hours"]);
        let docs_humidity = querySnapshot.docs.map(
          (doc) => doc.data()["humidity_predictions"]
        );
        let docs_uv = querySnapshot.docs.map(
          (doc) => doc.data()["uv_predictions"]
        );
        let docs_weather = querySnapshot.docs.map(
          (doc) => doc.data()["weather_predictions"]
        );

        this.setState({
          hours: docs_hours[0],
          humidity: docs_humidity[0],
          uv: docs_uv[0],
          weather: docs_weather[0],
        });
      });
  }

  render() {
    if (
      this.state.hours.length === 0 ||
      this.state.humidity.length === 0 ||
      this.state.uv.length === 0 ||
      this.state.weather.length === 0
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
          title="Next 24h"
          subtitle="Braga, Portugal"
          hours={this.state.hours}
          humidity={this.state.humidity}
          uv={this.state.uv}
          weather={this.state.weather}
        />
      </div>
    );
  }
}

export default BestPractice;
