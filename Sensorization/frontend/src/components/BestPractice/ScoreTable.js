import React from "react";

// reactstrap components
import {
  Card,
  CardHeader,
  CardBody,
  CardTitle,
  Col,
  Table,
  Row,
} from "reactstrap";

let formatLabels = (hours) => {
  return hours.map((hour) => {
    return `${hour}:00h`;
  });
};

let formatData = (data) => {
  return data.map((x) => {
    return x.toFixed(1);
  });
};

class ScoreTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      bestTimeHours: formatLabels(this.props.bestTimeHours),
      bestTimeHumidity: formatData(this.props.bestTimeHumidity),
      bestTimeUv: formatData(this.props.bestTimeUv),
      bestTimeWeather: formatData(this.props.bestTimeWeather),
      notBadTimeHours: formatLabels(this.props.notBadTimeHours),
      notBadTimeHumidity: formatData(this.props.notBadTimeHumidity),
      notBadTimeUv: formatData(this.props.notBadTimeUv),
      notBadTimeWeather: formatData(this.props.notBadTimeWeather),
    };
  }
  render() {
    return (
      <div>
        <Row>
          <Col md="6">
            <Card>
              <CardHeader>
                <CardTitle tag="h3">Advised train periods</CardTitle>
              </CardHeader>
              <CardBody>
                <Table className="tablesorter" responsive>
                  <thead className="text-primary">
                    <tr>
                      <th></th>
                      {this.state.bestTimeHours.map((hour) => (
                        <th className="text-secondary text-center"> {hour}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        Weather (ºC)
                      </th>
                      {this.state.bestTimeWeather.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        Humidity (%)
                      </th>
                      {this.state.bestTimeHumidity.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        UV Index (uv)
                      </th>
                      {this.state.bestTimeUv.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                  </tbody>
                </Table>
              </CardBody>
            </Card>
          </Col>
          <Col md="6">
            <Card>
              <CardHeader>
                <CardTitle tag="h3">Acceptable train periods</CardTitle>
              </CardHeader>
              <CardBody>
                <Table className="tablesorter" responsive>
                  <thead className="text-primary">
                    <tr>
                      <th></th>
                      {this.state.notBadTimeHours.map((hour) => (
                        <th className="text-secondary text-center"> {hour}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        Weather (ºC)
                      </th>
                      {this.state.notBadTimeWeather.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        Humidity (%)
                      </th>
                      {this.state.notBadTimeHumidity.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                    <tr>
                      <th className="text-secondary text-center" scope="row">
                        UV Index (uv)
                      </th>
                      {this.state.notBadTimeUv.map((value) => (
                        <td className="text-center">{`${value}`}</td>
                      ))}
                    </tr>
                  </tbody>
                </Table>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}

export default ScoreTable;
