import React from "react";

// reactstrap components
import { Card, CardHeader, CardBody, CardTitle, Col, Table } from "reactstrap";

let formatLabels = (docs_ids) => {
  return docs_ids.map((id) => {
    let date = new Date(id);

    let x = new String(date.getHours());
    if (x == "NaN") return new String("-");

    let hours = date.getHours();
    hours = date.getMinutes() > 30 ? hours + 1 : hours;

    let hourString = hours > 9 ? hours : `0${hours}`; // garantir que tem sempre 2 digitios para as colunas da tabela fiquem iguais
    let day = id.slice(8, 10);
    let month = id.slice(5, 7);

    return `${day}/${month} ${hourString}:00h `;
  });
};

class UVTable extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      labels: formatLabels(this.props.labels),
      st1: this.props.st1,
      st2: this.props.st2,
      st3: this.props.st3,
      st4: this.props.st4,
      st5: this.props.st5,
      st6: this.props.st6,
    };
  }
  render() {
    return (
      <Col md="12">
        <Card>
          <CardHeader>
            <CardTitle tag="h3">STs</CardTitle>
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
                  <th className="text-secondary text-center" scope="row">
                    St1
                  </th>
                  {this.state.st1.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    St2
                  </th>
                  {this.state.st2.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    St3
                  </th>
                  {this.state.st3.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    St4
                  </th>
                  {this.state.st4.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    St5
                  </th>
                  {this.state.st5.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
                <tr>
                  <th className="text-secondary text-center" scope="row">
                    St6
                  </th>
                  {this.state.st6.map((value) => (
                    <td className="text-center">{`${value}`}</td>
                  ))}
                </tr>
              </tbody>
            </Table>
          </CardBody>
        </Card>
      </Col>
    );
  }
}

export default UVTable;
