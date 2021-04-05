import { SemanticCOLORS } from "semantic-ui-react/dist/commonjs/generic";

const colorCycle: SemanticCOLORS[] =
    ['red', 'orange', 'yellow', 'olive', 'green', 'teal', 
    'blue','violet', 'purple', 'pink', 'brown', 'grey'];

const cycle: string[] = [];

export const GetColor = (code: string): SemanticCOLORS => {
    const lowerCode = code.toLowerCase();
    if (cycle.indexOf(lowerCode) === -1) {
        cycle.push(lowerCode);
    }
    return colorCycle[cycle.indexOf(lowerCode) % colorCycle.length];
}

export const ClearColor = () => {
    cycle.splice(0, cycle.length);
}
