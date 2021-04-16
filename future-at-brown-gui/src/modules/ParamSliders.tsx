import { Header} from "semantic-ui-react";
import { defaultParams, SearchParamNames, SearchParams } from "../classes/SearchParams";
import { useState } from "react";
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';
import User from "../classes/User";

//Parameters for a slider - the current user, a function to set the preference for the user, a function to signify loading.
interface Params {
    curUser?: User;
    prefChange?: (newPref: SearchParams) => void;
    setLoading?: (set: boolean) => void;
}

/**
 * Module representing the sliders for a our search page
 * @param props - the parameters as specified above.
 */
const ParamSlider: React.FC<Params> = (props) => {

    const [pref, setPrefs] = useState<SearchParams>(props.curUser?.getPreferences() ?? defaultParams);

    //Function to actually create a single slider.
    const makeSlider = (
        color: string,
        name: string,
        id: SearchParamNames
    ): JSX.Element => {
        return (
            <div style={{ padding: '1rem', display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
                <div style={{ width: '100%' }}>
                    <Header as="h5">
                        {name + ": "}
                        <em style={{color: color}}>
                            {pref[id]}
                        </em>
                    </Header>
                </div>
                <div style={{ width: '100%' }}>
                    <Slider
                        min={0}
                        max={10}
                        defaultValue={props.curUser?.getPreferences()[id]}
                        step={0.1}
                        trackStyle={{ backgroundColor: color }}
                        handleStyle={{ borderColor: color }}
                        dotStyle={{ borderColor: color }}
                        onChange={(newVal: number) => {
                            pref[id] = newVal;
                            setPrefs({ ...pref });
                        }}
                        onAfterChange={(newVal: number) => {
                            if (props.prefChange) {
                                pref[id] = newVal;
                                props.prefChange({...pref})
                            }
                        }}
                        marks={[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]}
                    />
                </div>

            </div>
        )
    }

    return (
        <div className={"sliders"}>
            <Header as="h4" content="Rate these options from 0 (least important) to 10 (most important)" />
            {makeSlider("red", "Low Average Hours", "avgHoursPref")}
            {makeSlider("goldenRod", "Low Max Hours", "maxHoursPref")}
            {makeSlider("springGreen", "High Class Rating", "crsRatingPref")}
            {makeSlider("deepSkyBlue", "High Professor Rating", "profRatingPref")}
            {makeSlider("fuchsia", "Small Class Size", "crsSizePref")}
            <div style={{height: "1rem"}} />
        </div>
    );
}

export default ParamSlider