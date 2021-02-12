# Cycling

Documents Cycling Activities

## Setup

To get an interactive development environment run:

    npm install
    lein figwheel

Include your Google Maps API Key as the sole Search Param:

    http://localhost:3449/?{apiKey}

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min