from enum import Enum


class FCondition(Enum):
    MIN = 1
    MAX = 2


def opposite_f_condition(cond: FCondition):
    if cond is FCondition.MIN:
        return FCondition.MAX
    elif cond is FCondition.MAX:
        return FCondition.MIN
    else:
        raise ValueError("Unknown f condition")


class AConditionB(Enum):
    LESS_OR_EQUAL = 1, "<="
    GREATER_OR_EQUAL = 2, ">="


def opposite_a_condition_b(cond: AConditionB):
    if cond is AConditionB.LESS_OR_EQUAL:
        return AConditionB.GREATER_OR_EQUAL
    elif cond is AConditionB.GREATER_OR_EQUAL:
        return AConditionB.LESS_OR_EQUAL
    else:
        raise ValueError("Unknown a condition b")