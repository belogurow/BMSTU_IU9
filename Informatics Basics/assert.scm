(use-syntax (ice-9 syncase))

(define test '())

(define-syntax use-assertions
  (syntax-rules ()
    ((_) (call-with-current-continuation (lambda (END) (set! test (lambda (x) (display "FAILED: ") (display x) (END))))))))

(define-syntax assert
  (syntax-rules ()
    ((_ expr) (if (not expr) (test 'expr)))))