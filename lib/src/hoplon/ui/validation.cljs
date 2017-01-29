(ns hoplon.ui.validation
  (:refer-clojure
    :exclude [integer?])
  (:require
    [javelin.core :refer [cell?]]
    [hoplon.ui.attrs :as a]))

;;; constants ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def globals       [:initial :inherit])
(def adjusts       [:none])
(def aligns        [:beg :mid :end])
(def haligns       [:jst])
(def valigns       [:baseline :sub :super :text-top :text-bottom])
(def blends        [:normal :color :color-burn :color-dodge :darken :difference
                    :exclusion :hard-light :hue :lighten :luminosity :multiply
                    :overlay :saturation :screen :soft-light :hue :saturation])
(def borders       [:none :hidden :dotted :dashed :solid :double :groove :ridge
                    :inset :outset])
(def colors        [:transparent :antiquewhite :aqua :aquamarine :azure :beige
                    :bisque :black :blanchedalmond :blue :blueviolet :brown
                    :burlywood :cadetblue :chartreuse :chocolate :coral
                    :cornflowerblue :cornsilk :crimson :darkblue :darkcyan
                    :darkgoldenrod :darkgray :darkgreen :darkgrey :darkkhaki
                    :darkmagenta :darkolivegreen :darkorange :darkorchid :darkred
                    :darksalmon :darkseagreen :darkslateblue :darkslategray
                    :darkslategrey :darkturquoise :darkviolet :deeppink
                    :deepskyblue :dimgray :dimgrey :dodgerblue :firebrick
                    :floralwhite :forestgreen :fuchsia :gainsboro :ghostwhite
                    :gold :goldenrod :gray :green :greenyellow :grey :honeydew
                    :hotpink :indianred :indigo :ivory :khaki :lavender
                    :lavenderblush :lawngreen :lemonchiffon :lightblue :lightcoral
                    :lightcyan :lightgoldenrodyellow :lightgray :lightgreen
                    :lightgrey :lightpink :lightsalmon :lightseagreen
                    :lightskyblue :lightslategray :lightslategrey :lightsteelblue
                    :lightyellow :lime :limegreen :linen :maroon :mediumaquamarine
                    :mediumblue :mediumorchid :mediumpurple :mediumseagreen
                    :mediumslateblue :mediumspringgreen :mediumturquoise
                    :mediumvioletred :midnightblue :mintcream :mistyrose :moccasin
                    :navajowhite :navy :oldlace :olive :olivedrab :orange :orangered
                    :orchid :palegoldenrod :palegreen :paleturquoise
                    :palevioletred :papayawhip :peachpuff :peru :pink :plum
                    :powderblue :purple :rebeccapurple :red :rosybrown :royalblue
                    :saddlebrown :salmon :sandybrown :seagreen :seashell :sienna
                    :silver :skyblue :slateblue :slategray :slategrey :snow
                    :springgreen :steelblue :tan :teal :thistle :tomato :turquoise
                    :violet :wheat :white :whitesmoke :yellow :yellowgreen])
(def cursors       [:alias :all-scroll :auto :cell :context-menu :col-resize :copy
                    :crosshair :default :e-resize :ew-resize :grab :grabbing :help
                    :move :n-resize :ne-resize :nesw-resize :ns-resize :nw-resize
                    :nwse-resize :no-drop :none :not-allowed :pointer :progress
                    :row-resize :s-resize :se-resize :sw-resize :text
                    :vertical-text :w-resize :wait :zoom-in :zoom-out])
(def extents       [:closest-side :closest-corner :farthest-side :farthest-corner])
(def decorations   [:none :underline :overline :line-through])
(def distributions [:flow :pile])
(def capitalizes   [:none :uppercase :lowercase :capitalize])
(def families      [:serif :sans-serif :monospace :cursive :fantasy])
(def kernings      [:auto :normal :none])
(def lengths       [:auto])
(def renderings    [:auto :optimizeSpeed :optimizeLegibility :geometricPrecision]) ;; todo: dash instead of camelcase
(def sizes         [:xx-small :x-small :small :medium :large :x-large :xx-large :larger :smaller])
(def smoothings    [:none :antialiased :subpixel-antialiased])
(def spacings      [:normal])
(def stretches     [:ultra-condensed :extra-condensed :condensed :semi-condensed :normal :semi-expanded :expanded :extra-expanded :ultra-expanded])
(def styles        [:normal :italic :oblique])
(def syntheses     [:none :weight :style :weight-style])
(def boxes         [:border :fill :view])
(def origins       [:left :right :top :bottom :center])
(def txstyles      [:preserve-3d :flat])
(def overflows     [:visible :hidden :scroll :auto])
(def weights       [:normal :bold :bolder :lighter :100 :200 :300 :400 :500 :600 :700 :800 :900])
(def fits          [:cover :contain :fill])

;;; utils ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn in? [v & kwvecs] (some #{v} (apply concat kwvecs)))

;;; validation fns ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn adjust? [v]
  (cond (keyword? v) (in? v adjusts globals)
        (number?  v) (and (>= v 0) (<= v 1)) ;; todo: make a ratio
        (nil?     v) :initial
        :else        false))

(defn align? [v]
  (cond (keyword? v) (in? v aligns lengths globals)
        (number?  v) v
        (nil?     v) :initial
        :else        false))

(defn alignh? [v]
  (cond (keyword? v) (in? v aligns haligns lengths globals)
        (number?  v) v
        (nil?     v) :initial
        :else        false))

(defn alignv? [v]
  (cond (keyword? v) (in? v aligns valigns lengths globals)
        (number?  v) v
        (nil?     v) :initial
        :else        false))

(defn blend? [v]
  (cond (keyword? v) (in? v blends)
        (nil?     v) true
        :else        false))

(defn callback? [v]
  (cond (fn? v)   v
        (nil?     v) true
        :else        false))

(defn border? [v]
  (cond (keyword? v) (in? v borders globals)
        (nil?     v) :initial
        :else        false))

(defn color? [v]
  (cond (keyword? v) (in? v colors globals)
        (a/color? v) v
        (nil?     v) :initial
        :else        false))

(defn cursor? [v]
  (cond (keyword? v) (in? v cursors globals)
        (nil?     v) :inital
        :else        false))

(defn distribution? [v]
  (cond (keyword? v) (in? v distributions)
        (nil?     v) true
        :else        false))

(defn dock? [v]
  (cond (keyword?  v) (in? v lengths globals)
        (a/calc?   v) v
        (a/ratio?  v) v
        (a/ems?    v) v
        (a/points? v) v
        (number?   v) v
        (true?     v) v
        (false?    v) true
        (nil?      v) :initial
        :else         false))

(defn extent? [v]
  (cond (keyword? v) (in? v extents)
        (nil?     v) true
        :else        false))

(defn family? [v]
  (cond (vector?  v) (every? identity v)
        (keyword? v) (in? v families globals)
        (string?  v) v
        (nil?     v) :initial
        :else        false))

(defn fit? [v]
  (cond (keyword? v) (in? v fits)
        (nil?     v) :fill
        :else        false))

(defn kerning? [v]
  (cond (keyword? v) (in? v kernings globals)
        (nil?     v) :initial
        :else        false))

(defn length? [v]
  (cond (keyword?  v) (in? v lengths globals)
        (a/calc?   v) v
        (a/ratio?  v) v
        (a/ems?    v) v
        (a/points? v) v
        (number?   v) v
        (nil?      v) :initial
        :else         false))

(defn opacity? [v]
  (cond (keyword? v) (in? v globals)
        (number?  v) (and (>= v 0) (<= v 1)) ;; todo: make a ratio
        (nil?     v) :initial
        :else        false))

(defn overflow? [v]
  (cond (keyword? v) (in? v overflows globals)
        (nil?     v) :initial
        :else        false))

(defn rendering? [v]
  (cond (keyword? v) (in? v renderings globals)
        (nil?     v) :initial
        :else        false))

(defn size? [v] ;; todo: support other units
  (cond (keyword?  v) (in? v sizes globals)
        (a/ratio?  v) v
        (number?   v) v
        (a/ems?    v) v
        (a/points? v) v
        (nil?      v) :initial
        :else         false))

(defn shadow? [v]
  (cond (vector?   v) (every? a/shadow? v)
        (keyword?  v) (in? v globals)
        (a/shadow? v) v
        (nil?      v) :initial
        :else         false))

(defn smoothing? [v]
  (cond (keyword? v) (in? v smoothings globals)
        (nil?     v) :initial
        :else        false))

(defn spacing? [v]
  (cond (keyword? v) (in? v spacings globals)
        (a/ratio? v) v
        (number?  v) v
        (nil?     v) :initial
        :else        false))

(defn stretch? [v]
  (cond (keyword? v) (in? v stretches globals)
        (nil?     v) :initial
        :else        false))

(defn style? [v]
  (cond (keyword? v) (in? v styles globals)
        (nil?     v) :initial
        :else        false))

(defn synthesis? [v]
  (cond (keyword? v) (in? v syntheses globals)
        (nil?     v) :initial
        :else        false))

(defn transform? [v]
  (cond (keyword?     v) (in? v globals)
        (a/transform? v) v
        (nil?         v) :initial
        :else            false))

(defn origin? [v]
  (cond (keyword?     v) (in? v origins globals)
        ;(a/ratio? v)      :initial
        (nil?         v) :initial
        :else            false))

(defn box? [v]
  (cond (keyword? v) (in? v boxes globals)
        (nil?     v) :initial
        :else        false))

(defn txstyle? [v]
  (cond (keyword? v) (in? v txstyles globals)
        (nil?     v) :initial
        :else        false))

(defn capitalize? [v]
  (cond (keyword? v) (in? v capitalizes globals)
        (nil?     v) :initial
        :else        false))

(defn decoration? [v]
  (cond (keyword? v) (in? v decorations globals)
        (nil?     v) :initial
        :else        false))

(defn weight? [v]
  (cond (keyword? v) (in? v weights globals)
        (nil?     v) :initial
        :else        false))

;;; form attributes ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def autocapitalizes [:none :sentences :words :characters])
(def autocompletes   [:off :on :additional-name :address-line1 :address-line2
                      :address-line3 :address-level4 :address-level3
                      :address-level2 :address-level1 :bday :bday-day
                      :bday-month :bday-year :cc-additional-name :cc-csc :cc-exp
                      :cc-exp-month :cc-exp-year :cc-family-name :cc-given-name
                      :cc-name :cc-number :cc-type :country :country-name
                      :current-password :email :family-name :given-name
                      :honorific-prefix :honorific-suffix :language :name
                      :new-password :nickname :organization :organization-title
                      :photo :postal-code :sex :street-address :tel
                      :transaction-amount :transaction-currency :username :url])
(def contents        [:text :search :tel :url :email :password])

(defn autocapitalize? [v]
  (cond (keyword? v) (in? v autocapitalizes)
        (nil?     v) true
        :else        false))

(defn autocomplete? [v]
  (cond (keyword? v) (in? v autocompletes)
        (nil?     v) true
        :else        false))

(defn content? [v]
  (cond (keyword? v) (in? v contents)
        (nil?     v) true
        :else        false))

(defn integer? [v]
  (cond (clojure.core/integer? v) v
        (nil?                  v) true
        :else                     false))

;;; validators ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn bind-cells [f] ;; todo: loop recur
  (fn [& vs]
    (let [watch (fn [i v] (if (cell? v) @(add-watch v i #(apply f (assoc (vec vs) i %4))) v))
          watch (fn [i v] (if (coll? v) (into (empty v) (map-indexed watch v)) (watch i v)))]
      (apply f (map-indexed watch vs)))))

(defn validate-cells [validator kind] ;; todo: refactor to include attribute key
  (fn [& vs]
    (doseq [v vs :let [valid? (bind-cells validator)]]
      (when-not (valid? v)
        (js/Error. "Error validating " kind " attribute with value "  v ".")))
    true))

(def adjusts?         (validate-cells adjust?         "adjust"))
(def aligns?          (validate-cells align?          "align"))
(def alignhs?         (validate-cells alignh?         "alingh"))
(def alignvs?         (validate-cells alignv?         "alignv"))
(def borders?         (validate-cells border?         "border"))
(def blends?          (validate-cells color?          "blend"))
(def colors?          (validate-cells color?          "color"))
(def cursors?         (validate-cells cursor?         "cursor"))
(def decorations?     (validate-cells decoration?     "decoration"))
(def distributions?   (validate-cells distribution?   "distribution"))
(def extents?         (validate-cells extent?         "extent"))
(def families?        (validate-cells family?         "family"))
(def fits?            (validate-cells fit?            "fit"))
(def kernings?        (validate-cells kerning?        "kerning"))
(def lengths?         (validate-cells length?         "length"))
(def opacities?       (validate-cells opacity?        "opacity"))
(def overflows?       (validate-cells overflow?       "overflow"))
(def renderings?      (validate-cells rendering?      "rendering"))
(def shadows?         (validate-cells shadow?         "shadow"))
(def sizes?           (validate-cells size?           "size"))
(def smoothings?      (validate-cells smoothing?      "smoothing"))
(def spacings?        (validate-cells spacing?        "spacing"))
(def stretches?       (validate-cells stretch?        "stetch"))
(def styles?          (validate-cells style?          "style"))
(def syntheses?       (validate-cells synthesis?      "sythesis"))
(def transforms?      (validate-cells transform?      "transformation"))
(def capitalizes?     (validate-cells capitalize?     "capitalize"))
(def origins?         (validate-cells origin?         "transformation origin"))
(def boxes?           (validate-cells box?            "transformation box"))
(def txstyles?        (validate-cells txstyle?        "transformation style"))
(def weights?         (validate-cells weight?         "weight"))

(def autocompletes?   (validate-cells autocomplete?   "autocomplete"))
(def autocapitalizes? (validate-cells autocapitalize? "autocapitalize"))
(def integers?        (validate-cells integer?        "integer"))
(def contents?        (validate-cells content?        "char"))

(def callbacks?       (validate-cells callback?       "callback"))
(def docks?           (validate-cells dock?           "dock"))
(def attrs?           (validate-cells empty?          "Unhandled attribute"))
