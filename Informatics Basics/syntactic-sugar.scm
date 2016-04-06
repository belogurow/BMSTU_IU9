(use-syntax (ice-9 syncase))

(define-syntax &
  (syntax-rules (->)
    ((_ -> x) (lambda () (begin x)))
    ((_ x -> b1 . b2) (lambda (x) (begin b1 . b2)))
    ((_ x1 x2 -> b1 . b2) (lambda (x1 x2) (begin b1 . b2)))))