// vite.config.js
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///D:/Project/java/WaterDetection/view/node_modules/vite/dist/node/index.js";
import vue from "file:///D:/Project/java/WaterDetection/view/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///D:/Project/java/WaterDetection/view/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///D:/Project/java/WaterDetection/view/node_modules/unplugin-vue-components/dist/vite.js";
import { ElementPlusResolver } from "file:///D:/Project/java/WaterDetection/view/node_modules/unplugin-vue-components/dist/resolvers.js";
import * as process from "file:///D:/Project/java/WaterDetection/view/node_modules/echarts/index.js";
import axios from "file:///D:/Project/java/WaterDetection/view/node_modules/axios/index.js";
var __vite_injected_original_import_meta_url = "file:///D:/Project/java/WaterDetection/view/vite.config.js";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
        //开启代理：在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJEOlxcXFxQcm9qZWN0XFxcXGphdmFcXFxcV2F0ZXJEZXRlY3Rpb25cXFxcdmlld1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiRDpcXFxcUHJvamVjdFxcXFxqYXZhXFxcXFdhdGVyRGV0ZWN0aW9uXFxcXHZpZXdcXFxcdml0ZS5jb25maWcuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0Q6L1Byb2plY3QvamF2YS9XYXRlckRldGVjdGlvbi92aWV3L3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHtmaWxlVVJMVG9QYXRoLCBVUkx9IGZyb20gJ25vZGU6dXJsJ1xuXG5pbXBvcnQge2RlZmluZUNvbmZpZ30gZnJvbSAndml0ZSdcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSAndW5wbHVnaW4tYXV0by1pbXBvcnQvdml0ZSdcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXG5pbXBvcnQge0VsZW1lbnRQbHVzUmVzb2x2ZXJ9IGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3Jlc29sdmVycydcbmltcG9ydCAqIGFzIHByb2Nlc3MgZnJvbSBcImVjaGFydHNcIjtcbmltcG9ydCBheGlvcyBmcm9tIFwiYXhpb3NcIjtcblxuLy9cbi8vICAgaW1wb3J0IGRhdGEgZnJvbSAnQC9hc3NldHMvY29uZmlnLmpzb24nO1xuXG5cbi8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuICAgIHBsdWdpbnM6IFtcbiAgICAgICAgdnVlKCksXG4gICAgICAgIEF1dG9JbXBvcnQoe1xuICAgICAgICAgICAgcmVzb2x2ZXJzOiBbRWxlbWVudFBsdXNSZXNvbHZlcigpXSxcbiAgICAgICAgfSksXG4gICAgICAgIENvbXBvbmVudHMoe1xuICAgICAgICAgICAgcmVzb2x2ZXJzOiBbRWxlbWVudFBsdXNSZXNvbHZlcigpXSxcbiAgICAgICAgfSlcbiAgICBdLFxuICAgIHJlc29sdmU6IHtcbiAgICAgICAgYWxpYXM6IHtcbiAgICAgICAgICAgICdAJzogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpXG4gICAgICAgIH1cbiAgICB9LFxuICAgIHNlcnZlcjoge1xuICAgICAgICBwcm94eToge1xuICAgICAgICAgICAgXCIvYXBpXCI6IHtcbiAgICAgICAgICAgICAgICB0YXJnZXQ6IFwiaHR0cDovL2xvY2FsaG9zdDo4MDgwXCIsXG4gICAgICAgICAgICAgICAgY2hhbmdlT3JpZ2luOiB0cnVlLFxuICAgICAgICAgICAgICAgIHNlY3VyZTogZmFsc2UsLy9cdTVGMDBcdTU0MkZcdTRFRTNcdTc0MDZcdUZGMUFcdTU3MjhcdTY3MkNcdTU3MzBcdTRGMUFcdTUyMUJcdTVFRkFcdTRFMDBcdTRFMkFcdTg2NUFcdTYyREZcdTY3MERcdTUyQTFcdTdBRUZcdUZGMENcdTcxMzZcdTU0MEVcdTUzRDFcdTkwMDFcdThCRjdcdTZDNDJcdTc2ODRcdTY1NzBcdTYzNkVcdUZGMENcdTVFNzZcdTU0MENcdTY1RjZcdTYzQTVcdTY1MzZcdThCRjdcdTZDNDJcbiAgICAgICAgICAgICAgICByZXdyaXRlOiAocGF0aCkgPT4gcGF0aC5yZXBsYWNlKC9eXFwvYXBpLywgJycpLFxuICAgICAgICAgICAgfVxuICAgICAgICB9XG4gICAgfVxufSlcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBcVMsU0FBUSxlQUFlLFdBQVU7QUFFdFUsU0FBUSxvQkFBbUI7QUFDM0IsT0FBTyxTQUFTO0FBQ2hCLE9BQU8sZ0JBQWdCO0FBQ3ZCLE9BQU8sZ0JBQWdCO0FBQ3ZCLFNBQVEsMkJBQTBCO0FBQ2xDLFlBQVksYUFBYTtBQUN6QixPQUFPLFdBQVc7QUFScUssSUFBTSwyQ0FBMkM7QUFleE8sSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDeEIsU0FBUztBQUFBLElBQ0wsSUFBSTtBQUFBLElBQ0osV0FBVztBQUFBLE1BQ1AsV0FBVyxDQUFDLG9CQUFvQixDQUFDO0FBQUEsSUFDckMsQ0FBQztBQUFBLElBQ0QsV0FBVztBQUFBLE1BQ1AsV0FBVyxDQUFDLG9CQUFvQixDQUFDO0FBQUEsSUFDckMsQ0FBQztBQUFBLEVBQ0w7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNMLE9BQU87QUFBQSxNQUNILEtBQUssY0FBYyxJQUFJLElBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsSUFDeEQ7QUFBQSxFQUNKO0FBQUEsRUFDQSxRQUFRO0FBQUEsSUFDSixPQUFPO0FBQUEsTUFDSCxRQUFRO0FBQUEsUUFDSixRQUFRO0FBQUEsUUFDUixjQUFjO0FBQUEsUUFDZCxRQUFRO0FBQUE7QUFBQSxRQUNSLFNBQVMsQ0FBQyxTQUFTLEtBQUssUUFBUSxVQUFVLEVBQUU7QUFBQSxNQUNoRDtBQUFBLElBQ0o7QUFBQSxFQUNKO0FBQ0osQ0FBQzsiLAogICJuYW1lcyI6IFtdCn0K
