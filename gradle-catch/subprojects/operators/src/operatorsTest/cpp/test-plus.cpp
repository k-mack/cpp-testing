#include "catch.hpp"
#include "operators.h"

TEST_CASE("Add 1 to 2 to get 3", "[unit]") {
    REQUIRE(plus(1, 2) == 3);
}
