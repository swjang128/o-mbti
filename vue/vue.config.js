module.exports = {
	outputDir: "../src/main/resources/static",
	indexPath: "../static/templates/index.html",
	devServer: {
		proxy: "http://127.0.0.1:1280"
	},
	chainWebpack: config => {
		const svgRule = config.module.rule("svg");
		svgRule.uses.clear();
		svgRule.use("vue-svg-loader").loader("vue-svg-loader");
	}
};