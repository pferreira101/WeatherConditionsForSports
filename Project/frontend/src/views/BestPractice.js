import React from "react";

// core components
import { Row, Col } from "reactstrap";
import PropagateLoader from "react-spinners/PropagateLoader";
import LineChart from "components/BestPractice/LineChart.js";
import ScoreTable from "components/BestPractice/ScoreTable.js";

// firestore
import firebase from "firebase/firestore.js";

let times = (hours, scores, humidity, uv, weather) => {
  let bestTimeHours = [];
  let bestTimeHumidity = [];
  let bestTimeUv = [];
  let bestTimeWeather = [];

  let notBadTimeHours = [];
  let notBadTimeHumidity = [];
  let notBadTimeUv = [];
  let notBadTimeWeather = [];

  let i;
  for (i = 0; i < hours.length; i++) {
    if (scores[i] === 3) {
      bestTimeHours.push(hours[i]);
      bestTimeHumidity.push(humidity[i]);
      bestTimeUv.push(uv[i]);
      bestTimeWeather.push(weather[i]);
    } else if (scores[i] === 2) {
      notBadTimeHours.push(hours[i]);
      notBadTimeHumidity.push(humidity[i]);
      notBadTimeUv.push(uv[i]);
      notBadTimeWeather.push(weather[i]);
    }
  }

  return {
    array0: bestTimeHours,
    array1: bestTimeHumidity,
    array2: bestTimeUv,
    array3: bestTimeWeather,
    array4: notBadTimeHours,
    array5: notBadTimeHumidity,
    array6: notBadTimeUv,
    array7: notBadTimeWeather,
  };
};

class BestPractice extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      hours: [],
      humidity: [],
      uv: [],
      weather: [],
      scores: [],
      bestTimeHours: [],
      bestTimeHumidity: [],
      bestTimeUv: [],
      bestTimeWeather: [],
      notBadTimeHours: [],
      notBadTimeHumidity: [],
      notBadTimeUv: [],
      notBadTimeWeather: [],
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
        let docs_scores = querySnapshot.docs.map((doc) => doc.data()["scores"]);

        let result = times(
          docs_hours.slice(-1)[0],
          docs_scores.slice(-1)[0],
          docs_humidity.slice(-1)[0],
          docs_uv.slice(-1)[0],
          docs_weather.slice(-1)[0]
        );

        this.setState({
          hours: docs_hours.slice(-1)[0],
          scores: docs_scores.slice(-1)[0],
          humidity: docs_humidity.slice(-1)[0],
          uv: docs_uv.slice(-1)[0],
          weather: docs_weather.slice(-1)[0],
          bestTimeHours: result.array0,
          bestTimeHumidity: result.array1,
          bestTimeUv: result.array2,
          bestTimeWeather: result.array3,
          notBadTimeHours: result.array4,
          notBadTimeHumidity: result.array5,
          notBadTimeUv: result.array6,
          notBadTimeWeather: result.array7,
        });
      });
  }

  render() {
    if (
      this.state.hours.length === 0 ||
      this.state.humidity.length === 0 ||
      this.state.uv.length === 0 ||
      this.state.weather.length === 0 ||
      this.state.scores.length === 0
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
          title="Next 24h Weather Predictions"
          subtitle="Braga, Portugal"
          hours={this.state.hours}
          humidity={this.state.humidity}
          uv={this.state.uv}
          weather={this.state.weather}
        />
        <ScoreTable
          bestTimeHours={this.state.bestTimeHours}
          bestTimeHumidity={this.state.bestTimeHumidity}
          bestTimeUv={this.state.bestTimeUv}
          bestTimeWeather={this.state.bestTimeWeather}
          notBadTimeHours={this.state.notBadTimeHours}
          notBadTimeHumidity={this.state.notBadTimeHumidity}
          notBadTimeUv={this.state.notBadTimeUv}
          notBadTimeWeather={this.state.notBadTimeWeather}
        />
      </div>
    );
  }
}

export default BestPractice;
