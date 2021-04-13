import { SemanticCOLORS } from "semantic-ui-react/dist/commonjs/generic";
import User from "./User"

const colorCycle: SemanticCOLORS[] =
    ['red', 'orange', 'yellow', 'olive', 'green', 'teal', 
    'blue','violet', 'purple', 'pink', 'brown', 'grey'];
const colorMap: {[name:string]:string} =
    {'red':'#B03060', 'orange':'#FE9A76', 'yellow':'#FFD700', 'olive':'#32CD32', 'green':'#016936', 'teal':'#008080',
        'blue':'#0E6EB8','violet':'#EE82EE', 'purple':'#B413EC', 'pink':'#FF1493', 'brown':'#A52A2A', 'grey':'#A0A0A0'};

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
