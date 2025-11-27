//==============================================================================
//
//                          CGG Services S.A.
//                          27, Rue Carnot
//                          91341 MASSY CEDEX
//
//------------------------------------------------------------------------------
//
// Copyright (C) CGG 2014
//==============================================================================
// $Id: //cypress/src/ecosystem/jenkins/trunk/geotest_matrix_dsl.groovy#16 $
// $Change: 582208 $
// $DateTime: 2017/01/03 02:19:33 $

// Original author: Michael Durocher <michael.durocher@cgg.com>
// Last change: $Author: vbricard $
matrixJob("geotest") {
  parameters {
    stringParam("VERSION", "")
    choiceParam("NEWREF",['false', 'true'])
  }
  publishers {
    flexiblePublish {
      conditionalAction {
        condition {
          booleanCondition('$NEWREF')
        }
        aggregationCondition {
          alwaysRun()
        }
        publishers {
          downstreamParameterized {
            trigger("geotest") {
              condition("SUCCESS")
              parameters {
                currentBuild()
                sameNode()
                booleanParam("NEWREF", false)
              }
            }
          }
        }
      }
    }
  }
}
