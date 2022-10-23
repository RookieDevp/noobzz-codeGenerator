import clipboard from './clipboard'

const install = function(Vue) {
  Vue.directive('clipboard', clipboard)
}
export default install
