import { useGetWeatherForecastQuery } from "./api/client/Query";

export const WeatherForecast = () => {
    const weatherQuery = useGetWeatherForecastQuery();

    return (
        <>
            {weatherQuery.isFetching && "Fetching..."}
            {weatherQuery.isError && "Error"}
            {weatherQuery.isSuccess && (
                <ul>
                    {weatherQuery.data.map((d, i) => (
                        <li key={i}>{d.summary}</li>
                    ))}
                </ul>
            )}
            <button onClick={() => weatherQuery.refetch()}>Refresh</button>
        </>
    );
};
