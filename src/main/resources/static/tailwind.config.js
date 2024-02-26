const plugin = require("tailwindcss/plugin")
const themes = require("daisyui/src/theming/themes")
module.exports = {
  content: ["./src/**/*.{astro,html,svelte,vue,js,ts,jsx,tsx}"],
  daisyui: {
    themes: [
      {
        light: {
          ...themes["light"],
            primary: "#7EC8E3",
            secondary: "#F2A65A",
            accent: "#FF9A8D",
            neutral: "#005377",
        },
        dark: {
          ...themes["dark"],
          primary: "#ffffff",
        },
      },
    ],

    // themes: [
    //   {
    //     acid: {
    //       ...require("../../theming/themes")["acid"],
    //       primary: "red",
    //     },
    //   },
    // ],
    // styled: 0,
    // base: 0,
    // utils: 0,
    // logs: true,
    // prefix: "b-",
    // darkTheme: "dark",
    // themeRoot: "*",
  },

  plugins: [
    require("@tailwindcss/forms"),
    require("@tailwindcss/typography"),
    require("../../"),
    // require("daisyui"),
    // require("tailwindcss-flip"),
  ],
}
