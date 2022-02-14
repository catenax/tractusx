// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import * as React from "react";
import { Row } from "react-bootstrap";
import { BsCheckCircle, BsCircle} from "react-icons/bs";

const BulletList = ({ text, icon = "checkCircle" }) => (
  <div className="content-items">
    <Row>
      <div className="col-1">
        {(icon === 'checkCircle') ? <BsCheckCircle color="#0F71CB" className="check-circle" /> : <BsCircle color="#0F71CB" className="check-circle" />}
      </div>
      <div className="col-11 bullet-points">{text}</div>
    </Row>
  </div>
);

export default BulletList;
