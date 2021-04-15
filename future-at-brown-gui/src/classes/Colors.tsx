import { SemanticCOLORS } from "semantic-ui-react/dist/commonjs/generic";
import User from "./User"

const colorCycle: SemanticCOLORS[] =
    ['blue','violet', 'purple', 'pink', 'red', 'orange',
    'yellow', 'olive', 'green', 'teal', 'brown', 'grey'];
const colorMap: {[name:string]:string} =
    {'red':'#db2828', 'orange':'#f2711c', 'yellow':'#fbbd08', 'olive':'#b5cc18', 'green':'#21ba45', 'teal':'#00b5ad',
        'blue':'#2185d0','violet':'#6435c9', 'purple':'#a333c8', 'pink':'#e03997', 'brown':'#a5673f', 'grey':'#767676'};

const cycle: string[] = [];

export function GetColor (code: string): SemanticCOLORS {
    const lowerCode = code.toLowerCase();
    if (cycle.indexOf(lowerCode) === -1) {
        cycle.push(lowerCode);
    }
    return colorCycle[cycle.indexOf(lowerCode) % colorCycle.length];
}

export function GetColorRaw (code: string): String {
    const lowerCode = code.toLowerCase();
    if (cycle.indexOf(lowerCode) === -1) {
        cycle.push(lowerCode);
    }
    let colorName = colorCycle[cycle.indexOf(lowerCode) % colorCycle.length];

    return colorMap[colorName];
}



export const ClearColor = () => {
    cycle.splice(0, cycle.length);
}
