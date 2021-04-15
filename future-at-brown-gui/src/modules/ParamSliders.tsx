import React, { useEffect } from "react";
import { Header} from "semantic-ui-react";
import { defaultParams, SearchParamNames, SearchParams } from "../classes/SearchParams";
import { useState } from "react";
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';
import User from "../classes/User";

interface Params {
    params?: SearchParams;
    curUser?: User;
    prefChange?: (newPref: SearchParams) => void;
    setLoading?: (set: boolean) => void;
}

const ParamSlider: React.FC<Params> = (props) => {

    const [pref, setPrefs] = useState<SearchParams>(
        props.params ?? props.curUser?.getPreferences() ?? defaultParams);

    useEffect(() => console.log("pref rerender", pref));
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
                        defaultValue={pref[id]}
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
                                console.log("b4", pref);
                                pref[id] = newVal;
                                console.log(pref);
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
            <Header as="h4" content="Rank these options from 0 (least important) to 10 (most important)" />
            {makeSlider("red", "Average Hours", "avgHoursPref")}
            {makeSlider("goldenRod", "Max Hours", "maxHoursPref")}
            {makeSlider("springGreen", "Class Rating", "crsRatingPref")}
            {makeSlider("deepSkyBlue", "Professor Rating", "profRatingPref")}
            {makeSlider("fuchsia", "Class Size", "crsSizePref")}
            <div style={{height: "1rem"}} />
        </div>
    );
}

export default ParamSlider