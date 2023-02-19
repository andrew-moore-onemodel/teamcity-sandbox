import "./App.css";

import { QueryClientProvider, QueryClient } from "@tanstack/react-query";
import { WeatherForecast } from "./WeatherForecast";
import { StrictMode } from "react";
import { setBaseUrl } from "./api/client/helpers";

const queryClient = new QueryClient();

setBaseUrl("https://localhost:7290");

function App() {
    return (
        <StrictMode>
            <QueryClientProvider client={queryClient}>
                <WeatherForecast />
            </QueryClientProvider>
        </StrictMode>
    );
}

export default App;
