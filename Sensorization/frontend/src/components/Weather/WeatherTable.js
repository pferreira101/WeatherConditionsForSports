import React from "react";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Col, Table } from "reactstrap";

let formatLabels = (docs_ids) => {
  return docs_ids.map((id) => {
    let date = new Date(id);

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    let hourString = hours > 9 ? hours : `0${hours}`; // garantir que tem sempre 2 digitios para as colunas da tabela fiquem iguais
    let day = id.slice(8, 10);
    let month = id.slice(5, 7);

    return `${day}/${month} ${hourString}:00h `;
  });
};

class WeatherTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      labels: formatLabels(this.props.labels),
      icons: this.props.icons,
      humidityData: this.props.humidityData,
      windSpeedData: this.props.windSpeedData,
      windDegData: this.props.windDegData,
    };
  }
  render() {
    return (
      <Col md="12">
        <Card>
          <CardHeader>
            <CardTitle tag="h3">Conditions</CardTitle>
          </CardHeader>
          <CardBody>
            <Table className="tablesorter" responsive>
              <thead className="text-primary">
                <tr>
                  <th></th>
                  {this.state.labels.map((label) => (
                    <th className="text-secondary text-center"> {label}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                <tr>
                  <th className="text-secondary" scope="row">
                    Condition
                  </th>
                  {this.state.icons.map((icon) => (
                    <td>
                      <img
                        src={`http://openweathermap.org/img/wn/${icon}@2x.png`}
                      />
                    </td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Humidity (%)
                  </th>
                  {this.state.humidityData.map((humidity) => (
                    <td className="text-center">{`${humidity}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Wind Speed (km/h)
                  </th>
                  {this.state.windSpeedData.map((windSpeed) => (
                    <td className="text-center">
                      {(windSpeed * 3.6).toFixed(1)}
                    </td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    Wind Direction
                  </th>
                  {this.state.windDegData.map((windDeg) =>
                    windDeg === "-" ? (
                      <td className="text-center">-</td>
                    ) : (
                      <td>
                        <img
                          style={{ transform: `rotate(${windDeg + 90}deg)` }}
                          src={`https://svn.apache.org/repos/asf/openoffice/symphony/trunk/main/extras/source/gallery/arrows/A07-Arrow-LightBlue-Left.png`}
                        />
                      </td>
                    )
                  )}
                </tr>
              </tbody>
            </Table>
          </CardBody>
        </Card>
      </Col>
    );
  }
}

export default WeatherTable;
